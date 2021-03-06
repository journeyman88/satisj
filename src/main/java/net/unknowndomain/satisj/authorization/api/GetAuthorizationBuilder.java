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

import net.unknowndomain.satisj.common.SatisApi;
import net.unknowndomain.satisj.common.SatisCallBuilder;

/**
 *
 * @author journeyman
 */
public class GetAuthorizationBuilder extends SatisCallBuilder<GetAuthorization>
{
    private String id;
    
    public GetAuthorizationBuilder(SatisApi api)
    {
        super(api);
    }
    
    /**
     * Sets the id to use for Authorization retrive.
     * The id of the authorization token.
     * 
     * @param id
     * @return this builder.
     */
    public GetAuthorizationBuilder id(String id)
    {
        this.id = id;
        return this;
    }
    
    @Override
    public GetAuthorization build()
    {
        return new GetAuthorization(api, id);
    }
}
