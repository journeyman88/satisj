/*
 * Copyright 2022 journeyman.
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
package net.unknowndomain.satisj.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 *
 * @author journeyman
 */
@JsonIgnoreProperties
public class AuthenticationKey
{
    @JsonProperty("access_key")
    private String accessKey;
    @JsonProperty("customer_uid")
    private String customerUid;
    @JsonProperty("customer_id")
    private String customerId;
    @JsonProperty("provider_id")
    private String providerId;
    @JsonProperty("key_type")
    private String keyType;
    @JsonProperty("auth_type")
    private String authType;
    @JsonProperty("role")
    private String role;
    @JsonProperty("enable")
    private Boolean enable;
    @JsonProperty("version")
    private Integer version;
    @JsonProperty("insert_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Date insertDate;

    public String getAccessKey()
    {
        return accessKey;
    }

    public void setAccessKey(String accessKey)
    {
        this.accessKey = accessKey;
    }

    public String getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }

    public String getKeyType()
    {
        return keyType;
    }

    public void setKeyType(String keyType)
    {
        this.keyType = keyType;
    }

    public String getAuthType()
    {
        return authType;
    }

    public void setAuthType(String authType)
    {
        this.authType = authType;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public Boolean getEnable()
    {
        return enable;
    }

    public void setEnable(Boolean enable)
    {
        this.enable = enable;
    }

    public Integer getVersion()
    {
        return version;
    }

    public void setVersion(Integer version)
    {
        this.version = version;
    }

    public Date getInsertDate()
    {
        return insertDate;
    }

    public void setInsertDate(Date insertDate)
    {
        this.insertDate = insertDate;
    }

    public String getProviderId()
    {
        return providerId;
    }

    public void setProviderId(String providerId)
    {
        this.providerId = providerId;
    }

    public String getCustomerUid()
    {
        return customerUid;
    }

    public void setCustomerUid(String customerUid)
    {
        this.customerUid = customerUid;
    }
    
}
