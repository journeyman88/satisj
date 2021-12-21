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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import net.unknowndomain.satisj.SatisApiCall;
import net.unknowndomain.satisj.payment.Payment;
import okhttp3.Response;

/**
 *
 * @author journeyman
 */
public class CreatePayment extends SatisApiCall<Payment> {
    
    @JsonProperty("flow")
    private final String flow;
    @JsonProperty("amount_unit")
    private final Long amountUnit;
    @JsonProperty("currency")
    private final String currency;
    @JsonProperty("pre_authorized_payments_token")
    private final String preAuthorizedPaymentsToken;
    @JsonProperty("parent_payment_uid")
    private final String parentPaymentId;
    @JsonProperty("consumer_uid")
    private final String consumerId;
    @JsonProperty("external_code")
    private final String externalCode;
    @JsonProperty("callback_url")
    private final String callbackUrl;
    @JsonProperty("redirect_url")
    private final String redirectUrl;
    @JsonProperty("metadata")
    private final Map<String, String> metadata;
    @JsonProperty("expiration_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "UTC")
    private final Date expirationDate;
    @JsonIgnore
    private final BigDecimal amount;
    
    protected CreatePayment(
            String flow, 
            BigDecimal amount,
            String currency,
            String preAuthorizedPaymentsToken,
            String parentPaymentUid,
            String consumerUid,
            String externalCode,
            String callbackUrl,
            String redirectUrl,
            Date expirationDate,
            Map<String, String> metadata
            )
    {
        this.amount = amount;
        this.expirationDate = expirationDate;
        this.flow = flow;
        this.currency = currency;
        this.preAuthorizedPaymentsToken = preAuthorizedPaymentsToken;
        this.parentPaymentId = parentPaymentUid;
        this.consumerId = consumerUid;
        this.externalCode = externalCode;
        this.callbackUrl = callbackUrl;
        this.redirectUrl = redirectUrl;
        this.amountUnit = amount.unscaledValue().longValue();
        this.metadata = Collections.unmodifiableMap(metadata);
    }
    
    @Override
    @JsonIgnore
    protected String getBody() {
        ObjectMapper objectMapper = new ObjectMapper();
        String body = "";
        try
        {
            body = objectMapper.writeValueAsString(this);
        }
        catch (JsonProcessingException ex)
        {
        }
        return body;
    }

    @Override
    @JsonIgnore
    protected String getMethod() {
        return "POST";
    }

    @Override
    @JsonIgnore
    protected String getRelativeEndpoint() {
        return "/v1/payments";
    }

    @Override
    @JsonIgnore
    protected Payment parseResponse(Response response)
    {
        response.body();
        return null;
    }

    public Date getExpirationDate()
    {
        return expirationDate;
    }

    public String getFlow()
    {
        return flow;
    }

    public Long getAmountUnit()
    {
        return amountUnit;
    }

    public String getCurrency()
    {
        return currency;
    }

    public String getPreAuthorizedPaymentsToken()
    {
        return preAuthorizedPaymentsToken;
    }

    public String getParentPaymentId()
    {
        return parentPaymentId;
    }

    public String getConsumerId()
    {
        return consumerId;
    }

    public String getExternalCode()
    {
        return externalCode;
    }

    public String getCallbackUrl()
    {
        return callbackUrl;
    }

    public String getRedirectUrl()
    {
        return redirectUrl;
    }

    public Map<String, String> getMetadata()
    {
        return metadata;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }
    
}
