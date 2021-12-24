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

import java.util.HashMap;
import java.util.Map;
import net.unknowndomain.satisj.common.SatisApi;
import net.unknowndomain.satisj.common.SatisCallBuilder;

/**
 *
 * @author journeyman
 */
public class CreateAuthorizationBuilder extends SatisCallBuilder<CreateAuthorization>
{
    private String reason;
    private String callbackUrl;
    private Map<String, String> metadata = new HashMap<>();
    
    public CreateAuthorizationBuilder(SatisApi api)
    {
        super(api);
    }
    
    /**
     * Sets the reason of the Authorization.
     * The reason why the token is being request.
     * 
     * @param reason
     * @return this builder.
     */
    public CreateAuthorizationBuilder reason(String reason)
    {
        this.reason = reason;
        return this;
    }
    
    /**
     * Sets the callbackUrl of the Authorization.
     * The url that will be called with an http GET request if the 
     * pre-authorization status changes. Note that {uuid} will be replaced 
     * with the authorization token.
     * 
     * @param callbackUrl
     * @return this builder.
     */
    public CreateAuthorizationBuilder callbackUrl(String callbackUrl)
    {
        this.callbackUrl = callbackUrl;
        return this;
    }
    
    /**
     * Sets the metadata of the Authorization.
     * Generic field that can be used to store generic info. The field 
     * phone_number can be used to pre-fill the mobile number. If integrating 
     * the Web-Redirect redirect_url is mandatory.
     * 
     * @param metadata
     * @return 
     */
    public CreateAuthorizationBuilder metadata(Map<String,String> metadata)
    {
        this.metadata = new HashMap<>();
        this.metadata.putAll(metadata);
        return this;
    }
    
    @Override
    public CreateAuthorization build()
    {
        return new CreateAuthorization(api, reason, callbackUrl, metadata);
    }
}
