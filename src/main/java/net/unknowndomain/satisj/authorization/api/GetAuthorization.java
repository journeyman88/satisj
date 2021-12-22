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
package net.unknowndomain.satisj.authorization.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.IOException;
import java.io.InputStream;
import net.unknowndomain.satisj.SatisApi;
import net.unknowndomain.satisj.SatisApiCall;
import net.unknowndomain.satisj.authorization.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author journeyman
 */
public class GetAuthorization extends SatisApiCall<Authorization> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GetAuthorization.class);
    
    private final String id;
    
    protected GetAuthorization(String id)
    {
        this.id = id;
    }
    
    @Override
    @JsonIgnore
    protected String getBody() {
        return "";
    }

    @Override
    @JsonIgnore
    protected String getMethod() {
        return "GET";
    }

    @Override
    @JsonIgnore
    protected String getRelativeEndpoint() {
        return "/v1/pre_authorized_payment_tokens/" + id;
    }

    @Override
    @JsonIgnore
    protected Authorization parseResponse(InputStream response)
    {
        Authorization payment = null;
        try
        {
            payment = SatisApi.Tools.JSON_MAPPER.readValue(response, Authorization.class);
        }
        catch (IOException ex)
        {
            LOGGER.error(null, ex);
        }
        return payment;
    }

    public String getId()
    {
        return id;
    }
    
}
