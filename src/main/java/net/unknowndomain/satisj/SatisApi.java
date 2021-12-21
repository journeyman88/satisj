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
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Date;
import net.unknowndomain.satisj.auth.SatisAuth;
import net.unknowndomain.satisj.common.SatisError;
import net.unknowndomain.satisj.consumer.retrieve.RetrieveConsumerBuilder;
import net.unknowndomain.satisj.payment.create.CreatePaymentBuilder;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.codec.binary.Base64;
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
    private final static FastDateFormat SIGN_DATE_FORMAT = FastDateFormat.getInstance("EEE, dd MMM yyyy HH:mm:ss Z");
    private final static MediaType JSON = MediaType.get("application/json; charset=utf-8");
    
    public SatisApi(Environment env, SatisAuth auth){
        this.auth = auth;
        this.env = env;
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
                    SIGN_DATE_FORMAT.format(data),
                    digest);
            Signature sig = Signature.getInstance("SHA256WithRSA");
            sig.initSign(auth.getPrivateKey());
            sig.update(toSign.getBytes(StandardCharsets.UTF_8));
            String signature = Base64.encodeBase64String(sig.sign());
            Request.Builder bld = new Request.Builder();
            bld.url(env.getEndpoint() + call.getRelativeEndpoint());
            bld.addHeader("Host", env.getEndpoint().getHost());
            bld.addHeader("Date", SIGN_DATE_FORMAT.format(data));
            bld.addHeader("Digest", "SHA-256="+digest);
            bld.addHeader("Authorization", String.format("Signature keyId=\"%s\", algorithm=\"rsa-sha256\", headers=\"(request-target) host date digest\", signature=\"%s\"", auth.getKeyId(), signature));
            bld.addHeader("Idempotency-Key", call.getIdempotencyKey());
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
            if (resp.code() == 200)
            {
                return call.parseResponse(resp);
            }
            return new SatisError();
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
    }
    
}