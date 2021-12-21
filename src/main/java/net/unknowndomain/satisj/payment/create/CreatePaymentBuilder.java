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
package net.unknowndomain.satisj.payment.create;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.unknowndomain.satisj.SatisCallBuilder;

/**
 *
 * @author journeyman
 */
public class CreatePaymentBuilder implements SatisCallBuilder<CreatePayment>
{
    private String flow = "MATCH_CODE";
    private BigDecimal amount = BigDecimal.ZERO;
    private String currency = "EUR";
    private String preAuthorizedPaymentsToken;
    private String parentPaymentUid;
    private String consumerUid;
    private String externalCode;
    private String callbackUrl;
    private String redirectUrl;
    private Date expirationDate;
    private Map<String, String> metadata = new HashMap<>();
    
    public CreatePaymentBuilder matchCode()
    {
        flow = "MATCH_CODE";
        return this;
    }
    
    public CreatePaymentBuilder matchUser(String consumerUid)
    {
        flow = "MATCH_USER";
        this.preAuthorizedPaymentsToken = null;
        this.parentPaymentUid = null;
        this.consumerUid = consumerUid;
        return this;
    }
    
    public CreatePaymentBuilder refund(String parentPaymentUid)
    {
        flow = "REFUND";
        this.preAuthorizedPaymentsToken = null;
        this.parentPaymentUid = parentPaymentUid;
        this.consumerUid = null;
        return this;
    }
    
    public CreatePaymentBuilder preAuthorized(String preAuthorizedPaymentsToken)
    {
        flow = "PRE_AUTHORIZED";
        this.preAuthorizedPaymentsToken = preAuthorizedPaymentsToken;
        this.parentPaymentUid = null;
        this.consumerUid = null;
        return this;
    }
    
    public CreatePaymentBuilder amount(BigDecimal amount)
    {
        this.amount = amount;
        return this;
    }
    
    public CreatePaymentBuilder currency(String currency)
    {
        this.currency = currency;
        return this;
    }
    
    /**
     * Sets the externalCode of the CreatePayment.
     * Order ID or payment external identifier (max length allowed is 50 chars).
     * @param externalCode
     * @return this builder.
     */
    public CreatePaymentBuilder externalCode(String externalCode)
    {
        this.externalCode = externalCode;
        return this;
    }
    
    /**
     * Sets the callbackUrl of the CreatePayment.
     * The url that will be called with an http GET request when the payment changes state. 
     * When url is called a Get payment details can be called to know the new Payment status. 
     * Note that {uuid} will be replaced with the Payment ID
     * @param callbackUrl
     * @return this builder.
     */
    public CreatePaymentBuilder callbackUrl(String callbackUrl)
    {
        this.callbackUrl = callbackUrl;
        return this;
    }
    
    /**
     * Sets the redirectUrl of the CreatePayment.
     * The url to redirect the user after the payment flow is completed.
     * @param redirectUrl
     * @return this builder.
     */
    public CreatePaymentBuilder redirectUrl(String redirectUrl)
    {
        this.redirectUrl = redirectUrl;
        return this;
    }
    
    public CreatePaymentBuilder expirationDate(Date expirationDate)
    {
        this.expirationDate = expirationDate;
        return this;
    }
    
    public CreatePaymentBuilder metadata(Map<String,String> metadata)
    {
        this.metadata = new HashMap<>();
        this.metadata.putAll(metadata);
        return this;
    }
    
    @Override
    public CreatePayment build()
    {
        return new CreatePayment(flow, amount, currency, preAuthorizedPaymentsToken, parentPaymentUid, consumerUid, externalCode, callbackUrl, redirectUrl, expirationDate, metadata);
    }
}
