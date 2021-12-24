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
import net.unknowndomain.satisj.common.SatisJsonObject;

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
    @JsonProperty("status_ownership")
    private boolean statusOwnership;
    @JsonProperty("status_owner")
    private Actor statusOwner;
    @JsonProperty("receiver")
    private Actor receiver;
    @JsonProperty("sender")
    private Actor sender;
    
//    daily_closure [object]: The daily closure of the payment
//        id [string]: ID of the daily closure
//        date [string]: The closure date

    
    public String getId()
    {
        return id;
    }

    public String getCodeIdentifier()
    {
        return codeIdentifier;
    }

    public Long getAmountUnit()
    {
        return amountUnit;
    }

    public String getCurrency()
    {
        return currency;
    }

    public PaymentType getType()
    {
        return type;
    }

    public PaymentStatus getStatus()
    {
        return status;
    }

    public boolean isExpired()
    {
        return expired;
    }

    public Map<String, String> getMetadata()
    {
        return metadata;
    }

    public String getInsertDate()
    {
        return insertDate;
    }

    public String getExpireDate()
    {
        return expireDate;
    }

    public String getExternalCode()
    {
        return externalCode;
    }

    public String getRedirectUrl()
    {
        return redirectUrl;
    }

    public boolean isStatusOwnership()
    {
        return statusOwnership;
    }

    public Actor getStatusOwner()
    {
        return statusOwner;
    }

    public Actor getReceiver()
    {
        return receiver;
    }

    public Actor getSender()
    {
        return sender;
    }
}
