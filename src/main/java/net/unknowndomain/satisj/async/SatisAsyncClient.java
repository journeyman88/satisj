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
package net.unknowndomain.satisj.async;

import net.unknowndomain.satisj.common.SatisApiCall;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import net.unknowndomain.satisj.Environment;
import net.unknowndomain.satisj.async.SatisJsonConsumer;
import net.unknowndomain.satisj.auth.SatisAuth;
import net.unknowndomain.satisj.common.SatisApi;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.nio.support.AsyncRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author journeyman
 */
public class SatisAsyncClient extends SatisApi {
    private final static Logger LOGGER = LoggerFactory.getLogger(SatisAsyncClient.class);

    public SatisAsyncClient(Environment env, SatisAuth auth)
    {
        super(env, auth);
    }
    
    @Override
    protected <T> T execCall(SatisApiCall call, Class<T> clazz)
    {
        try
        {
            return queueCall(call, clazz).get();
        } 
        catch (InterruptedException | ExecutionException ex)
        {
            LOGGER.error(null, ex);
        }
        return null;
    }
    
    protected <T> Future <T> queueCall(SatisApiCall call, Class<T> clazz)
    {
        Future <T> retVal = null;
        try (CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault()) {
            httpClient.start();
            String body = call.getBody();
            AsyncRequestBuilder bld = AsyncRequestBuilder.create(call.getMethod());
            Map<String, String> headers = prepareHeaders(call, body);
            for (String key : headers.keySet())
            {
                bld.addHeader(key, headers.get(key));
            }
            bld.setEntity(body, ContentType.APPLICATION_JSON.withCharset(StandardCharsets.UTF_8));
            SatisJsonConsumer cons = new SatisJsonConsumer(clazz);
            retVal = httpClient.execute(bld.build(), cons, null);
        } 
        catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | IOException ex)
        {
            LOGGER.error(null, ex);
        }
        return retVal;
    }
}
