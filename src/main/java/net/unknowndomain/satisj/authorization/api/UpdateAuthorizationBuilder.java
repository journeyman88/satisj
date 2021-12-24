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
public class UpdateAuthorizationBuilder extends SatisCallBuilder<UpdateAuthorization>
{
    private String id;
    private String status = "CANCELED";
    private String consumerId;
    private Map<String, String> metadata = new HashMap<>();
    
    public UpdateAuthorizationBuilder(SatisApi api)
    {
        super(api);
    }
    
    /**
     * Sets the id to use for Authorization update.
     * The id of the authorization token.
     * 
     * @param id
     * @return this builder.
     */
    public UpdateAuthorizationBuilder id(String id)
    {
        this.id = id;
        return this;
    }
    
    /**
     * Mark the Authorization as cancelled.
     * 
     * @return this builder.
     */
    public UpdateAuthorizationBuilder cancel()
    {
        this.status = "CANCELED";
        return this;
    }
    
    /**
     * Sets the consumerId of the Authorization.
     * Unique ID of the consumer used to bind the token.
     * 
     * @param consumerId
     * @return this builder.
     */
    public UpdateAuthorizationBuilder consumerId(String consumerId)
    {
        this.consumerId = consumerId;
        return this;
    }
    
    /**
     * Sets the metadata of the Authorization.
     * Generic field that can be used to store additional data.
     * 
     * @param metadata
     * @return 
     */
    public UpdateAuthorizationBuilder metadata(Map<String,String> metadata)
    {
        this.metadata = new HashMap<>();
        this.metadata.putAll(metadata);
        return this;
    }
    
    @Override
    public UpdateAuthorization build()
    {
        return new UpdateAuthorization(api, id, status, consumerId, metadata);
    }
}
