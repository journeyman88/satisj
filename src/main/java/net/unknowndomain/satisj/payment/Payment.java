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
package net.unknowndomain.satisj.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import net.unknowndomain.satisj.SatisJsonObject;

/**
 *
 * @author journeyman
 */
public class Payment extends SatisJsonObject
{
    public Payment()
    {
        super(false);
    }
    
    @JsonProperty("id")
    private String id;
    @JsonProperty("code_identifier")
    private String codeIdentifier;
    @JsonProperty("amount_unit")
    private Long amountUnit;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("type")
    private PaymentType type;
    @JsonProperty("status")
    private PaymentStatus status;
    @JsonProperty("expired")
    private boolean expired;
    @JsonProperty("metadata")
    private Map<String, String> metadata;
    @JsonProperty("insert_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "UTC")
    private String insertDate;
    @JsonProperty("expire_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "UTC")
    private String expireDate;
    @JsonProperty("external_code")
    private String externalCode;
    @JsonProperty("redirect_url")
    private String redirectUrl;
    
//    sender [object]: The sender actor of the payment
//        id [string]: Unique ID of the sender
//        type [string]: Type of the actor (CONSUMER or SHOP)
//        name [string]: Short name of the actor
//    receiver [object]: The receiver actor of the payment
//        id [string]: Unique ID of the receiver
//        type [string]: Type of the actor (SHOP or CONSUMER)
//    daily_closure [object]: The daily closure of the payment
//        id [string]: ID of the daily closure
//        date [string]: The closure date
//    status_ownership [boolean]: If true, the device making the request is responsible for the final status reached by the payment
//    status_owner [object]: The actor responsible of the payment final status
//        id [string]: Unique ID of the actor
//        type[string]: Type of the actor (DEVICE)

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getCodeIdentifier()
    {
        return codeIdentifier;
    }

    public void setCodeIdentifier(String codeIdentifier)
    {
        this.codeIdentifier = codeIdentifier;
    }

    public Long getAmountUnit()
    {
        return amountUnit;
    }

    public void setAmountUnit(Long amountUnit)
    {
        this.amountUnit = amountUnit;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public PaymentType getType()
    {
        return type;
    }

    public void setType(PaymentType type)
    {
        this.type = type;
    }

    public PaymentStatus getStatus()
    {
        return status;
    }

    public void setStatus(PaymentStatus status)
    {
        this.status = status;
    }

    public boolean isExpired()
    {
        return expired;
    }

    public void setExpired(boolean expired)
    {
        this.expired = expired;
    }

    public Map<String, String> getMetadata()
    {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata)
    {
        this.metadata = metadata;
    }

    public String getInsertDate()
    {
        return insertDate;
    }

    public void setInsertDate(String insertDate)
    {
        this.insertDate = insertDate;
    }

    public String getExpireDate()
    {
        return expireDate;
    }

    public void setExpireDate(String expireDate)
    {
        this.expireDate = expireDate;
    }

    public String getExternalCode()
    {
        return externalCode;
    }

    public void setExternalCode(String externalCode)
    {
        this.externalCode = externalCode;
    }

    public String getRedirectUrl()
    {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl)
    {
        this.redirectUrl = redirectUrl;
    }


}
