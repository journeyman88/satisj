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

import net.unknowndomain.satisj.common.SatisApiCall;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import net.unknowndomain.satisj.auth.SatisAuth;
import net.unknowndomain.satisj.common.SatisApi;
import net.unknowndomain.satisj.common.SatisApiException;
import net.unknowndomain.satisj.common.SatisError;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author journeyman
 */
public class SatisSimpleClient extends SatisApi {
    private final static Logger LOGGER = LoggerFactory.getLogger(SatisApi.class);

    public SatisSimpleClient(Environment env, SatisAuth auth)
    {
        super(env, auth);
    }
    
    @Override
    protected <T> T execCall(SatisApiCall call, Class<T> clazz)
    {
        T retVal = null;
        try
        {
            retVal = buildCall(call, clazz).call();
        } 
        catch (Exception ex)
        {
            LOGGER.error(null, ex);
        }
        return retVal;
    }
    
    @Override
    protected <T> Future<T> queueCall(SatisApiCall call, Class<T> clazz)
    {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        return executorService.submit(buildCall(call, clazz));
    }

    @Override
    protected <T> Callable<T> buildCall(SatisApiCall call, Class<T> clazz)
    {
        return () ->
        {
            T retVal = null;
            try (final CloseableHttpClient httpClient = HttpClients.createDefault())
            {
                String body = call.getBody();
                ClassicRequestBuilder bld = ClassicRequestBuilder.create(call.getMethod());
                Map<String, String> headers = prepareHeaders(call, body);
                for (String key : headers.keySet())
                {
                    bld.addHeader(key, headers.get(key));
                }
                bld.setUri(call.getUrl(env).toString());
                bld.setEntity(body, ContentType.APPLICATION_JSON.withCharset(StandardCharsets.UTF_8));
                ClassicHttpResponse resp = httpClient.execute(bld.build());
                try (final InputStream bodyStream = resp.getEntity().getContent())
                {
                    if (resp.getCode() == 200)
                    {
                        retVal = Tools.JSON_MAPPER.readValue(bodyStream, clazz);
                    } else
                    {
                        throw new SatisApiException(Tools.JSON_MAPPER.readValue(bodyStream, SatisError.class));
                    }
                }
            }catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | IOException ex)
            {
                LOGGER.error(null, ex);
            }
            return retVal;
        };
    }
}
