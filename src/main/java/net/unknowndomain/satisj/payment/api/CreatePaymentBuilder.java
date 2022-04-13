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
package net.unknowndomain.satisj.payment.api;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.unknowndomain.satisj.common.SatisApi;
import net.unknowndomain.satisj.common.SatisCallBuilder;

/**
 * API call builder to initialize a Payment
 * 
 * @author journeyman
 */
public class CreatePaymentBuilder extends SatisCallBuilder<CreatePayment>
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
    
    public CreatePaymentBuilder(SatisApi api)
    {
        super(api);
    }
    
    /**
     * Selects the "MATCH_CODE" payment flow.
     * @return this builder
     */
    public CreatePaymentBuilder matchCode()
    {
        flow = "MATCH_CODE";
        this.preAuthorizedPaymentsToken = null;
        this.parentPaymentUid = null;
        this.consumerUid = null;
        return this;
    }
    
    /**
     * Selects the "MATCH_USER" payment flow.
     * @param consumerUid the user unique identifier, which can be retrieved by a call on Consumer API
     * @return this builder
     */
    public CreatePaymentBuilder matchUser(String consumerUid)
    {
        flow = "MATCH_USER";
        this.preAuthorizedPaymentsToken = null;
        this.parentPaymentUid = null;
        this.consumerUid = consumerUid;
        return this;
    }
    
    /**
     * Selects the "REFUND" payment flow.
     * @param parentPaymentUid the unique identifier of the Payment to be refunded.
     * @return this builder
     */
    public CreatePaymentBuilder refund(String parentPaymentUid)
    {
        flow = "REFUND";
        this.preAuthorizedPaymentsToken = null;
        this.parentPaymentUid = parentPaymentUid;
        this.consumerUid = null;
        return this;
    }
    
    /**
     * Selects the "PRE_AUTHORIZED" payment flow.
     * @param preAuthorizedPaymentsToken the token of the pre-authorized transaction.
     * @return this builder
     */
    public CreatePaymentBuilder preAuthorized(String preAuthorizedPaymentsToken)
    {
        flow = "PRE_AUTHORIZED";
        this.preAuthorizedPaymentsToken = preAuthorizedPaymentsToken;
        this.parentPaymentUid = null;
        this.consumerUid = null;
        return this;
    }
    
    /**
     * Selects the "FUND_LOCK" payment flow.
     * @return this builder
     */
    public CreatePaymentBuilder fundLock()
    {
        flow = "FUND_LOCK";
        this.preAuthorizedPaymentsToken = null;
        this.parentPaymentUid = null;
        this.consumerUid = null;
        return this;
    }
    
    /**
     * Sets the total amount of the transaction.
     * @param amount Amount of the payment
     * @return this builder
     */
    public CreatePaymentBuilder amount(BigDecimal amount)
    {
        this.amount = amount;
        return this;
    }
    
    
    /**
     * Sets the currency of the transaction.
     * The currency is both  required by the Satispay system and by the internal converters to
     * obtain the minimal unit to convert the amount.
     * @param currency Currency of the payment (only EUR currently supported)
     * @return this builder
     */
    public CreatePaymentBuilder currency(String currency)
    {
        this.currency = currency;
        return this;
    }
    
    /**
     * Sets the externalCode of the CreatePayment.
     * @param externalCode Order ID or payment external identifier (max length allowed is 50 chars).
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
     * @param callbackUrl the callbackUrl, which should contain the {uuid} token
     * @return this builder.
     */
    public CreatePaymentBuilder callbackUrl(String callbackUrl)
    {
        this.callbackUrl = callbackUrl;
        return this;
    }
    
    /**
     * Sets the redirectUrl of the CreatePayment.
     * @param redirectUrl The url to redirect the user after the payment flow is completed.
     * @return this builder.
     */
    public CreatePaymentBuilder redirectUrl(String redirectUrl)
    {
        this.redirectUrl = redirectUrl;
        return this;
    }
    
    /**
     * Sets the transaction's expiration date of the CreatePayment.
     * The url to redirect the user after the payment flow is completed.
     * @param expirationDate The expiration date of the payment
     * @return this builder.
     */
    public CreatePaymentBuilder expirationDate(Date expirationDate)
    {
        this.expirationDate = expirationDate;
        return this;
    }
    
    /**
     * Sets the metadata field.
     * @param metadata Generic field that can be used to store generic info. The field phone_number can be used to pre-fill the mobile number.
     * @return this builder.
     */
    public CreatePaymentBuilder metadata(Map<String,String> metadata)
    {
        this.metadata = new HashMap<>();
        this.metadata.putAll(metadata);
        return this;
    }
    
    @Override
    public CreatePayment build()
    {
        return new CreatePayment(api, flow, amount, currency, preAuthorizedPaymentsToken, parentPaymentUid, consumerUid, externalCode, callbackUrl, redirectUrl, expirationDate, metadata);
    }
}
