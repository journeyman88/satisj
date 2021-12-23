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

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import net.unknowndomain.satisj.common.SatisApiException;

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
    
    protected abstract String getBody();
    protected abstract String getMethod();
    protected abstract String getEndpoint(Environment env);
    
    protected URL getUrl(Environment env) throws MalformedURLException
    {
        return new URL(env.getEndpoint().getProtocol(), env.getEndpoint().getHost(), getEndpoint(env));
    }
    
    public T execute() throws SatisApiException, IOException
    {
        return SatisApi.Tools.JSON_MAPPER.readValue(api.execCall(this), clazz);
    }
    
}
