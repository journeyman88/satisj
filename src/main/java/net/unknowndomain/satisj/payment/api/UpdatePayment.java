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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import net.unknowndomain.satisj.Environment;
import net.unknowndomain.satisj.SatisApi;
import net.unknowndomain.satisj.SatisApiCall;
import net.unknowndomain.satisj.payment.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author journeyman
 */
public class UpdatePayment extends SatisApiCall<Payment> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdatePayment.class);
    
    @JsonProperty("action")
    private final String action;
    @JsonProperty("amount_unit")
    private final Long amountUnit;
    @JsonProperty("metadata")
    private final Map<String, String> metadata;
    @JsonIgnore
    private final BigDecimal amount;
    @JsonIgnore
    private final String id;
    @JsonIgnore
    private final String currency;
    
    protected UpdatePayment(
            SatisApi api,
            String id, 
            BigDecimal amount,
            String currency,
            String action,
            Map<String, String> metadata
            )
    {
        super(api, Payment.class);
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.action = action;
        this.amountUnit = SatisApi.Tools.getUnits(currency, amount);
        this.metadata = Collections.unmodifiableMap(metadata);
    }
    
    @Override
    @JsonIgnore
    protected String getBody() {
        String body = "";
        try
        {
            body = SatisApi.Tools.JSON_MAPPER.writeValueAsString(this);
        }
        catch (JsonProcessingException ex)
        {
            LOGGER.error(null, ex);
        }
        return body;
    }

    @Override
    @JsonIgnore
    protected String getMethod() {
        return "PUT";
    }

    @Override
    @JsonIgnore
    protected String getEndpoint(Environment env) {
        return env.getEndpoint().getPath() + "/v1/payments/" + id;
    }

//    @Override
//    @JsonIgnore
//    protected Payment parseResponse(InputStream response)
//    {
//        Payment payment = null;
//        try
//        {
//            payment = SatisApi.Tools.JSON_MAPPER.readValue(response, Payment.class);
//        }
//        catch (IOException ex)
//        {
//            LOGGER.error(null, ex);
//        }
//        return payment;
//    }

    public Long getAmountUnit()
    {
        return amountUnit;
    }

    public Map<String, String> getMetadata()
    {
        return metadata;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public String getAction()
    {
        return action;
    }

    public String getId()
    {
        return id;
    }

    public String getCurrency()
    {
        return currency;
    }
    
}
