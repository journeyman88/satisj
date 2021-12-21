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
    
    private final BigDecimal amount;
    private final Date expirationDate;
    private final Request req;
    
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
        this.req = new Request();
        this.req.flow = flow;
        this.req.currency = currency;
        this.req.pre_authorized_payments_token = preAuthorizedPaymentsToken;
        this.req.parent_payment_uid = parentPaymentUid;
        this.req.consumer_uid = consumerUid;
        this.req.external_code = externalCode;
        this.req.callback_url = callbackUrl;
        this.req.redirect_url = redirectUrl;
        this.req.amount_unit = amount.unscaledValue().longValue();
        this.req.expiration_date = DATE_FORMATTER.format(expirationDate);
        this.req.metadata = Collections.unmodifiableMap(metadata);
    }
    
    @Override
    protected String getBody() {
//        Moshi moshi = new Moshi.Builder().build();
//        JsonAdapter<Request> adapter = moshi.adapter(Request.class);
//        return adapter.toJson(req);
        return "";
    }

    @Override
    protected String getMethod() {
        return "POST";
    }

    @Override
    protected String getRelativeEndpoint() {
        return "/v1/payments";
    }

    @Override
    protected Payment parseResponse(Response response)
    {
        response.body();
        return null;
    }
    
    private class Request {
        
        private String flow;
        private Long amount_unit;
        private String currency;
        private String pre_authorized_payments_token;
        private String parent_payment_uid;
        private String consumer_uid;
        private String external_code;
        private String callback_url;
        private String redirect_url;
        private String expiration_date;
        private Map<String, String> metadata;
        
    }

    public String getFlow()
    {
        return req.flow;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public String getCurrency()
    {
        return req.currency;
    }

    public String getPreAuthorizedPaymentsToken()
    {
        return req.pre_authorized_payments_token;
    }

    public String getParentPaymentUid()
    {
        return req.parent_payment_uid;
    }

    public String getConsumerUid()
    {
        return req.consumer_uid;
    }

    public String getExternalCode()
    {
        return req.external_code;
    }

    public String getCallbackUrl()
    {
        return req.callback_url;
    }

    public String getRedirectUrl()
    {
        return req.redirect_url;
    }

    public Date getExpirationDate()
    {
        return expirationDate;
    }

    public Map<String, String> getMetadata()
    {
        return req.metadata;
    }
    
}
