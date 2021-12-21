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


}
