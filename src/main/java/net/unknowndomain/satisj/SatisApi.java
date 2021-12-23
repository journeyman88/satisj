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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Date;
import net.unknowndomain.satisj.auth.SatisAuth;
import net.unknowndomain.satisj.authorization.api.CreateAuthorizationBuilder;
import net.unknowndomain.satisj.authorization.api.GetAuthorizationBuilder;
import net.unknowndomain.satisj.authorization.api.UpdateAuthorizationBuilder;
import net.unknowndomain.satisj.common.SatisApiException;
import net.unknowndomain.satisj.common.SatisBasicApi;
import net.unknowndomain.satisj.common.SatisError;
import net.unknowndomain.satisj.consumer.api.RetrieveConsumerBuilder;
import net.unknowndomain.satisj.payment.api.CreatePaymentBuilder;
import net.unknowndomain.satisj.payment.api.PaymentDetailsBuilder;
import net.unknowndomain.satisj.payment.api.UpdatePaymentBuilder;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicClassicHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author journeyman
 */
public class SatisApi extends SatisBasicApi {
    private final static Logger LOGGER = LoggerFactory.getLogger(SatisApi.class);
    private final ConsumerApi consumerApi = new ConsumerApi(this);
    private final PaymentApi paymentApi = new PaymentApi(this);
    private final AuthorizationApi authorizationApi = new AuthorizationApi(this);

    public SatisApi(Environment env, SatisAuth auth)
    {
        super(env, auth);
    }
    
    protected InputStream execCall(SatisApiCall call) throws SatisApiException
    {
        InputStream retVal = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String body = call.getBody();
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
            BasicClassicHttpRequest req = null;
            switch(call.getMethod())
            {
                case "GET":
                    req = new HttpGet(call.getUrl(env).toString());
                    break;
                case "POST":
                    req = new HttpPost(call.getUrl(env).toString());
                    break;
                case "PUT":
                    req = new HttpPut(call.getUrl(env).toString());
                    break;
                case "DELETE":
                    req = new HttpDelete(call.getUrl(env).toString());
                    break;
                case "PATCH":
                    req = new HttpPatch(call.getUrl(env).toString());
                    break;
            }
            req.addHeader("User-Agent", USER_AGENT);
            req.addHeader("Host", env.getEndpoint().getHost());
            req.addHeader("Date", Tools.SIGN_DATE_FORMAT.format(data));
            req.addHeader("Digest", "SHA-256="+digest);
            req.addHeader("Authorization", String.format("Signature keyId=\"%s\", algorithm=\"rsa-sha256\", headers=\"(request-target) host date digest\", signature=\"%s\"", auth.getKeyId(), signature));
            req.addHeader("Idempotency-Key", call.getIdempotencyKey());
            if (StringUtils.isNotBlank(platformName))
            {
                req.addHeader("x-satispay-os", platformName);
            }
            if (StringUtils.isNotBlank(platformVersion))
            {
                req.addHeader("x-satispay-osv", platformVersion);
            }
            if (StringUtils.isNotBlank(appName))
            {
                req.addHeader("x-satispay-appn", appName);
            }
            if (StringUtils.isNotBlank(appVersion))
            {
                req.addHeader("x-satispay-appv", appVersion);
            }
            if (StringUtils.isNotBlank(appName))
            {
                req.addHeader("x-satispay-devicetype", deviceType);
            }
            if (StringUtils.isNotBlank(trackingCode))
            {
                req.addHeader("x-satispay-tracking-code", trackingCode);
            }
            HttpEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON.withCharset(StandardCharsets.UTF_8));
            req.setEntity(entity);
            ClassicHttpResponse resp = httpClient.execute(req);
            try (InputStream bodyStream = resp.getEntity().getContent())
            {
                if (resp.getCode() == 200)
                {
                    retVal = bodyStream;
                }
                throw new SatisApiException(Tools.JSON_MAPPER.readValue(bodyStream, SatisError.class));
            }
        } 
        catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | IOException ex)
        {
            LOGGER.error(null, ex);
        }
        return retVal;
    }
    
    protected static class PaymentApi {
        
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
    
    protected static class ConsumerApi {
        
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
    
    protected static class AuthorizationApi {
        
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
