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

import net.unknowndomain.satisj.SatisCallBuilder;

/**
 *
 * @author journeyman
 */
public class PaymentDetailsBuilder implements SatisCallBuilder<PaymentDetails>
{
    private String id = "";
    
    /**
     * Sets the Payment ID for retrieval.
     * @param id The id of the payment to retrieve
     * @return 
     */
    public PaymentDetailsBuilder id(String id)
    {
        this.id = id;
        return this;
    }
    
    @Override
    public PaymentDetails build()
    {
        return new PaymentDetails(id);
    }
}
