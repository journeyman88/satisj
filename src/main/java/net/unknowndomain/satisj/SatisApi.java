/*
 * Copyright 2021 Marco Bignami.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.unknowndomain.satisj;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import net.unknowndomain.satisj.auth.SatisAuth;
import net.unknowndomain.satisj.authorization.api.CreateAuthorizationBuilder;
import net.unknowndomain.satisj.authorization.api.GetAuthorizationBuilder;
import net.unknowndomain.satisj.authorization.api.UpdateAuthorizationBuilder;
import net.unknowndomain.satisj.common.SatisError;
import net.unknowndomain.satisj.consumer.api.RetrieveConsumerBuilder;
import net.unknowndomain.satisj.payment.api.CreatePaymentBuilder;
import net.unknowndomain.satisj.payment.api.PaymentDetailsBuilder;
import net.unknowndomain.satisj.payment.api.UpdatePaymentBuilder;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author journeyman
 */
public class SatisApi {
    private final SatisAuth auth;
    private final Environment env;
    private final OkHttpClient client = new OkHttpClient();
    private final static Logger LOGGER = LoggerFactory.getLogger(SatisApi.class);
    private final static MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final static String USER_AGENT;
    private String platformName;
    private String platformVersion;
    private String appName;
    private String appVersion;
    private String deviceType;
    private String trackingCode;
    
    static {
        Properties props = new Properties();
        try (InputStream inStream = SatisApi.class.getResourceAsStream("/net/unknowndomain/satisj/version.properties"))
        {
            props.load(inStream);
        } 
        catch (IOException ex)
        {
            LOGGER.error(null, ex);
        }
        USER_AGENT = props.getProperty("library.name", "StatisJ") + "/" + props.getProperty("library.version", "0.0.0");
    }
    
    public SatisApi(Environment env, SatisAuth auth){
        this.auth = auth;
        this.env = env;
    }
    
    /**
     * Register a new currency and sets the right-shift to conver amount to units.
     * 
     * @param currencyCode
     * @param shift 
     */
    public static void registerCurrency(String currencyCode, int shift)
    {
        Tools.registerCurrency(currencyCode, shift);
    }
    
    public SatisJsonObject execCall(SatisApiCall call)
    {
        try {
            String body = call.getBody();
            MessageDigest md = MessageDigest.getInstance("SHA256");
            String digest = Base64.encodeBase64String(md.digest(body.getBytes()));
            Date data = new Date();
            String toSign = String.format("(request-target): %s %s\nhost: %s\ndate: %s\ndigest: SHA-256=%s", 
                    call.getMethod().toLowerCase(),
                    env.getEndpoint().getPath() + call.getRelativeEndpoint(),
                    env.getEndpoint().getHost(),
                    Tools.SIGN_DATE_FORMAT.format(data),
                    digest);
            Signature sig = Signature.getInstance("SHA256WithRSA");
            sig.initSign(auth.getPrivateKey());
            sig.update(toSign.getBytes(StandardCharsets.UTF_8));
            String signature = Base64.encodeBase64String(sig.sign());
            Request.Builder bld = new Request.Builder();
            bld.url(env.getEndpoint() + call.getRelativeEndpoint());
            bld.addHeader("User-Agent", USER_AGENT);
            bld.addHeader("Host", env.getEndpoint().getHost());
            bld.addHeader("Date", Tools.SIGN_DATE_FORMAT.format(data));
            bld.addHeader("Digest", "SHA-256="+digest);
            bld.addHeader("Authorization", String.format("Signature keyId=\"%s\", algorithm=\"rsa-sha256\", headers=\"(request-target) host date digest\", signature=\"%s\"", auth.getKeyId(), signature));
            bld.addHeader("Idempotency-Key", call.getIdempotencyKey());
            if (StringUtils.isNotBlank(platformName))
            {
                bld.addHeader("x-satispay-os", platformName);
            }
            if (StringUtils.isNotBlank(platformVersion))
            {
                bld.addHeader("x-satispay-osv", platformVersion);
            }
            if (StringUtils.isNotBlank(appName))
            {
                bld.addHeader("x-satispay-appn", appName);
            }
            if (StringUtils.isNotBlank(appVersion))
            {
                bld.addHeader("x-satispay-appv", appVersion);
            }
            if (StringUtils.isNotBlank(appName))
            {
                bld.addHeader("x-satispay-devicetype", deviceType);
            }
            if (StringUtils.isNotBlank(trackingCode))
            {
                bld.addHeader("x-satispay-tracking-code", trackingCode);
            }
            RequestBody reqBody = RequestBody.create(body, JSON);
            switch(call.getMethod())
            {
                case "GET":
                    bld.get();
                    break;
                case "POST":
                    bld.post(reqBody);
                    break;
                case "PUT":
                    bld.put(reqBody);
                    break;
                case "DELETE":
                    bld.delete();
                    break;
                case "PATCH":
                    bld.patch(reqBody);
                    break;
            }
            Response resp = client.newCall(bld.build()).execute();
            try (InputStream bodyStream = resp.body().byteStream())
            {
                if (resp.code() == 200)
                {
                    return call.parseResponse(bodyStream);
                }
                return Tools.JSON_MAPPER.readValue(bodyStream, SatisError.class);
            }
        } 
        catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | IOException ex)
        {
            LOGGER.error(null, ex);
        }
        return null;
    }
    
    public static class ConsumerApi {
        public static RetrieveConsumerBuilder retrieve()
        {
            return new RetrieveConsumerBuilder();
        }
    }
    
    public static class PaymentApi {
        public static CreatePaymentBuilder create()
        {
            return new CreatePaymentBuilder();
        }
        public static PaymentDetailsBuilder details()
        {
            return new PaymentDetailsBuilder();
        }
        public static UpdatePaymentBuilder update()
        {
            return new UpdatePaymentBuilder();
        }
    }
    
    public static class AuthorizationApi {
        public static CreateAuthorizationBuilder create()
        {
            return new CreateAuthorizationBuilder();
        }
        public static GetAuthorizationBuilder retrieve()
        {
            return new GetAuthorizationBuilder();
        }
        public static UpdateAuthorizationBuilder update()
        {
            return new UpdateAuthorizationBuilder();
        }
    }
    public static class Tools
    {
        public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
        public static final FastDateFormat SIGN_DATE_FORMAT = FastDateFormat.getInstance("EEE, dd MMM yyyy HH:mm:ss Z");
        private static Map<String, Integer> CURRENCY_SHIFT = new HashMap<>();

        static
        {
            JSON_MAPPER.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            JSON_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            CURRENCY_SHIFT = new HashMap<>();
            registerCurrency("DEFAULT", 2);
            registerCurrency("EUR", 2);
        }
        
        private static void registerCurrency(String currencyCode, int shift)
        {
            if (currencyCode != null)
            {
                String curr = currencyCode.toUpperCase();
                Map<String, Integer> cs = new HashMap<>();
                cs.putAll(CURRENCY_SHIFT);
                cs.put(curr, shift);
                CURRENCY_SHIFT = Collections.unmodifiableMap(cs);
            }
        }

        public static Long getUnits(BigDecimal amount)
        {
            return getUnits(null, amount);
        }

        public static Long getUnits(String currencyCode, BigDecimal amount)
        {
            String curr = currencyCode;
            if ((curr == null) || (!CURRENCY_SHIFT.containsKey(curr)))
            {
                curr = "DEFAULT";
            }
            return amount.movePointRight(CURRENCY_SHIFT.get(curr)).longValue();
        }
    }

    public String getPlatformName()
    {
        return platformName;
    }

    public void setPlatformName(String platformName)
    {
        this.platformName = platformName;
    }

    public String getPlatformVersion()
    {
        return platformVersion;
    }

    public void setPlatformVersion(String platformVersion)
    {
        this.platformVersion = platformVersion;
    }

    public String getAppName()
    {
        return appName;
    }

    public void setAppName(String appName)
    {
        this.appName = appName;
    }

    public String getAppVersion()
    {
        return appVersion;
    }

    public void setAppVersion(String appVersion)
    {
        this.appVersion = appVersion;
    }

    public String getDeviceType()
    {
        return deviceType;
    }

    public void setDeviceType(String deviceType)
    {
        this.deviceType = deviceType;
    }

    public String getTrackingCode()
    {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode)
    {
        this.trackingCode = trackingCode;
    }
}
