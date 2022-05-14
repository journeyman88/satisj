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
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import net.unknowndomain.satisj.Environment;
import net.unknowndomain.satisj.auth.SatisAuth;
import net.unknowndomain.satisj.common.SatisApi;
import org.apache.hc.client5.http.async.HttpAsyncClient;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.protocol.HttpClientContext;
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
    private final HttpAsyncClient httpClient;

    public SatisAsyncClient(Environment env, SatisAuth auth)
    {
        super(env, auth);
        httpClient = HttpAsyncClients.createDefault();
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
    
    @Override
    protected <T> Future<T> queueCall(SatisApiCall call, Class<T> clazz)
    {
        Future<T> retVal = null;
        try
        {
            String body = call.getBody();
            AsyncRequestBuilder bld = AsyncRequestBuilder.create(call.getMethod());
            bld.setUri(call.getUrl(env).toString());
            Map<String, String> headers = prepareHeaders(call, body);
            for (String key : headers.keySet())
            {
                bld.addHeader(key, headers.get(key));
            }
            bld.setEntity(body, ContentType.APPLICATION_JSON.withCharset(StandardCharsets.UTF_8));
            SatisJsonConsumer cons = new SatisJsonConsumer(clazz);
            retVal = httpClient.execute(bld.build(), cons, null, HttpClientContext.create(), null);
        } 
        catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | MalformedURLException ex)
        {
            LOGGER.error(null, ex);
        }
        return retVal;
    }
    
    @Override
    protected <T> Callable <T> buildCall(SatisApiCall call, Class<T> clazz)
    {
        return () ->
        {
            T retVal = null;
            try (final CloseableHttpAsyncClient httpClient1 = HttpAsyncClients.createDefault())
            {
                httpClient1.start();
                String body = call.getBody();
                AsyncRequestBuilder bld = AsyncRequestBuilder.create(call.getMethod());
                bld.setUri(call.getUrl(env).toString());
                Map<String, String> headers = prepareHeaders(call, body);
                for (String key : headers.keySet())
                {
                    bld.addHeader(key, headers.get(key));
                }
                bld.setEntity(body, ContentType.APPLICATION_JSON.withCharset(StandardCharsets.UTF_8));
                SatisJsonConsumer cons = new SatisJsonConsumer(clazz);
                Future<T> future = httpClient1.execute(bld.build(), cons, null);
                retVal = future.get();
            }
            catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | IOException ex)
            {
                LOGGER.error(null, ex);
            }
            return retVal;
        };
    }
}
