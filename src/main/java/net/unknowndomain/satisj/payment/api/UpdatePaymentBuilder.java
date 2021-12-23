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
import java.util.HashMap;
import java.util.Map;
import net.unknowndomain.satisj.SatisApi;
import net.unknowndomain.satisj.SatisCallBuilder;

/**
 *
 * @author journeyman
 */
public class UpdatePaymentBuilder extends SatisCallBuilder<UpdatePayment>
{
    public UpdatePaymentBuilder(SatisApi api)
    {
        super(api);
    }
    
    private String action = "";
    private BigDecimal amount = BigDecimal.ZERO;
    private String currency = "EUR";
    private String id = "";
    private Map<String, String> metadata = new HashMap<>();
    
    public UpdatePaymentBuilder accept()
    {
        action = "ACCEPT";
        return this;
    }
    
    public UpdatePaymentBuilder cancel()
    {
        action = "CANCEL";
        return this;
    }
    
    public UpdatePaymentBuilder cancelOrRefund()
    {
        action = "CANCEL_OR_REFUND)";
        return this;
    }
    
    public UpdatePaymentBuilder preAuthorized(String id)
    {
        this.id = id;
        return this;
    }
    
    public UpdatePaymentBuilder amount(BigDecimal amount)
    {
        this.amount = amount;
        return this;
    }
    
    public UpdatePaymentBuilder currency(String currency)
    {
        this.currency = currency;
        return this;
    }
    
    public UpdatePaymentBuilder metadata(Map<String,String> metadata)
    {
        this.metadata = new HashMap<>();
        this.metadata.putAll(metadata);
        return this;
    }
    
    @Override
    public UpdatePayment build()
    {
        return new UpdatePayment(api, id, amount, currency, action, metadata);
    }
}
