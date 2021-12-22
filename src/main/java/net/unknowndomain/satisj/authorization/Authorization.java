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
package net.unknowndomain.satisj.authorization;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import net.unknowndomain.satisj.SatisJsonObject;

/**
 *
 * @author journeyman
 */
public class Authorization extends SatisJsonObject
{
    @JsonProperty("id")
    private String id;
    @JsonProperty("code_identifier")
    private String codeIdentifier;
    @JsonProperty("shop_uid")
    private String shopId;
    @JsonProperty("consumer_uid")
    private String consumerId;
    @JsonProperty("reason")
    private String reason;
    @JsonProperty("callback_url")
    private String callbackUrl;
    @JsonProperty("status")
    private AuthorizationStatus status;
    @JsonProperty("metadata")
    private Map<String, Object> metadata;
    
    public Authorization()
    {
        super(false);
    }


}
