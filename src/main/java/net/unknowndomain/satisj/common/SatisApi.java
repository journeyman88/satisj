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
package net.unknowndomain.satisj.common;

import net.unknowndomain.satisj.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
import java.util.concurrent.Future;
import net.unknowndomain.satisj.auth.SatisAuth;
import net.unknowndomain.satisj.authorization.api.CreateAuthorizationBuilder;
import net.unknowndomain.satisj.authorization.api.GetAuthorizationBuilder;
import net.unknowndomain.satisj.authorization.api.UpdateAuthorizationBuilder;
import net.unknowndomain.satisj.consumer.api.RetrieveConsumerBuilder;
import net.unknowndomain.satisj.payment.api.CreatePaymentBuilder;
import net.unknowndomain.satisj.payment.api.PaymentDetailsBuilder;
import net.unknowndomain.satisj.payment.api.UpdatePaymentBuilder;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author journeyman
 */
public abstract class SatisApi {
    protected final SatisAuth auth;
    protected final Environment env;
    private final static Logger LOGGER = LoggerFactory.getLogger(SatisApi.class);
    private final static String USER_AGENT;
    private final ConsumerApi consumerApi = new ConsumerApi(this);
    private final PaymentApi paymentApi = new PaymentApi(this);
    private final AuthorizationApi authorizationApi = new AuthorizationApi(this);
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
    
    public static class Tools
    {
        public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
        public static final FastDateFormat SIGN_DATE_FORMAT = FastDateFormat.getInstance("EEE, dd MMM yyyy HH:mm:ss Z");
        private static Map<String, Integer> CURRENCY_SHIFT = new HashMap<>();

        static
        {
            JSON_MAPPER.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            JSON_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            JSON_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
    
    protected abstract <T> T execCall(SatisApiCall call, Class<T> clazz);
    protected abstract <T> Future <T> queueCall(SatisApiCall call, Class<T> clazz);
    
    protected Map<String,String> prepareHeaders(SatisApiCall call, String body) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException
    {
        MessageDigest md = MessageDigest.getInstance("SHA256");
        String digest = Base64.encodeBase64String(md.digest(body.getBytes()));
        Date data = new Date();
        String toSign = String.format("(request-target): %s %s\nhost: %s\ndate: %s\ndigest: SHA-256=%s", 
                call.getMethod().toLowerCase(),
                call.getEndpoint(env),
                env.getEndpoint().getHost(),
                Tools.SIGN_DATE_FORMAT.format(data),
                digest);
        Signature sig = Signature.getInstance("SHA256WithRSA");
        sig.initSign(auth.getPrivateKey());
        sig.update(toSign.getBytes(StandardCharsets.UTF_8));
        String signature = Base64.encodeBase64String(sig.sign());
        Map<String,String> headers = new HashMap<>();
        headers.put("User-Agent", USER_AGENT);
        headers.put("Host", env.getEndpoint().getHost());
        headers.put("Date", Tools.SIGN_DATE_FORMAT.format(data));
        headers.put("Digest", "SHA-256="+digest);
        headers.put("Authorization", String.format("Signature keyId=\"%s\", algorithm=\"rsa-sha256\", headers=\"(request-target) host date digest\", signature=\"%s\"", auth.getKeyId(), signature));
        headers.put("Idempotency-Key", call.getIdempotencyKey());
        if (StringUtils.isNotBlank(platformName))
        {
            headers.put("x-satispay-os", platformName);
        }
        if (StringUtils.isNotBlank(platformVersion))
        {
            headers.put("x-satispay-osv", platformVersion);
        }
        if (StringUtils.isNotBlank(appName))
        {
            headers.put("x-satispay-appn", appName);
        }
        if (StringUtils.isNotBlank(appVersion))
        {
            headers.put("x-satispay-appv", appVersion);
        }
        if (StringUtils.isNotBlank(appName))
        {
            headers.put("x-satispay-devicetype", deviceType);
        }
        if (StringUtils.isNotBlank(trackingCode))
        {
            headers.put("x-satispay-tracking-code", trackingCode);
        }
        return headers;
    }
    
    public static class PaymentApi {
        
        private final SatisApi api;
        
        protected PaymentApi(SatisApi api)
        {
            this.api = api;
        }
        
        public CreatePaymentBuilder create()
        {
            return new CreatePaymentBuilder(api);
        }
        public PaymentDetailsBuilder retrieve()
        {
            return new PaymentDetailsBuilder(api);
        }
        public UpdatePaymentBuilder update()
        {
            return new UpdatePaymentBuilder(api);
        }
    }
    
    public static class ConsumerApi {
        
        private final SatisApi api;
        
        protected ConsumerApi(SatisApi api)
        {
            this.api = api;
        }
        
        public RetrieveConsumerBuilder create()
        {
            return new RetrieveConsumerBuilder(api);
        }
    }
    
    public static class AuthorizationApi {
        
        private final SatisApi api;
        
        protected AuthorizationApi(SatisApi api)
        {
            this.api = api;
        }
        
        public CreateAuthorizationBuilder create()
        {
            return new CreateAuthorizationBuilder(api);
        }
        public GetAuthorizationBuilder retrieve()
        {
            return new GetAuthorizationBuilder(api);
        }
        public UpdateAuthorizationBuilder update()
        {
            return new UpdateAuthorizationBuilder(api);
        }
    }

    public ConsumerApi consumer()
    {
        return consumerApi;
    }

    public PaymentApi payment()
    {
        return paymentApi;
    }

    public AuthorizationApi authorization()
    {
        return authorizationApi;
    }
}
