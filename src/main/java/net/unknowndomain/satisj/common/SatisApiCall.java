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

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.reactivex.Observable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.Future;
import net.unknowndomain.satisj.Environment;

/**
 *
 * @author journeyman
 * @param <T>
 */
public abstract class SatisApiCall<T extends SatisJsonObject> {
    
    @JsonIgnore
    private final SatisApi api;    
    @JsonIgnore
    private final Class<T> clazz;
    @JsonIgnore
    private final String idempotencyKey = UUID.randomUUID().toString();
    
    protected SatisApiCall(SatisApi api, Class<T> clazz)
    {
        this.api = api;
        this.clazz = clazz;
    }
    
    public String getIdempotencyKey()
    {
        return idempotencyKey;
    }
    
    public abstract String getBody();
    public abstract String getMethod();
    protected abstract String getEndpoint(Environment env);
    
    public URL getUrl(Environment env) throws MalformedURLException
    {
        return new URL(env.getEndpoint().getProtocol(), env.getEndpoint().getHost(), getEndpoint(env));
    }
    
    /**
     * Execute the API call in synchronous mode
     * @return the call result
     * @throws SatisApiException 
     */
    public T execute() throws SatisApiException
    {
        return api.execCall(this, clazz);
    }
    
    /**
     * Queue the API to be called in asynchronous mode
     * @return a Future containing the call result
     * @throws SatisApiException 
     */
    public Future <T> queue() throws SatisApiException
    {
        return api.queueCall(this, clazz);
    }
    
    /**
     * Wrap the the API call in a Reactive Observable
     * @return an Observable that represents the API call.
     * @throws SatisApiException 
     */
    public Observable<T> call()
    {
        return Observable.fromCallable(api.buildCall(this, clazz));
    }
    
}
