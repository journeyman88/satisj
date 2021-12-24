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
import net.unknowndomain.satisj.Environment;
import net.unknowndomain.satisj.common.SatisApi;
import net.unknowndomain.satisj.common.SatisApiCall;
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
    
    protected GetAuthorization(SatisApi api, String id)
    {
        super(api, Authorization.class);
        this.id = id;
    }
    
    @Override
    @JsonIgnore
    public String getBody() {
        return "";
    }

    @Override
    @JsonIgnore
    public String getMethod() {
        return "GET";
    }

    @Override
    @JsonIgnore
    protected String getEndpoint(Environment env) {
        return env.getEndpoint().getPath() + "/v1/pre_authorized_payment_tokens/" + id;
    }

//    @Override
//    @JsonIgnore
//    protected Authorization parseResponse(InputStream response)
//    {
//        Authorization payment = null;
//        try
//        {
//            payment = SatisApi.Tools.JSON_MAPPER.readValue(response, Authorization.class);
//        }
//        catch (IOException ex)
//        {
//            LOGGER.error(null, ex);
//        }
//        return payment;
//    }

    public String getId()
    {
        return id;
    }
    
}
