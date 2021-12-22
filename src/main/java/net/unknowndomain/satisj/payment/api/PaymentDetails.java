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

import java.io.IOException;
import java.io.InputStream;
import net.unknowndomain.satisj.SatisApi;
import net.unknowndomain.satisj.SatisApiCall;
import net.unknowndomain.satisj.payment.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author journeyman
 */
public class PaymentDetails extends SatisApiCall<Payment> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentDetails.class);
    
    private final String id;
    
    protected PaymentDetails(String id)
    {
        this.id = id;
    }
    
    @Override
    protected String getBody() {
        return "";
    }

    @Override
    protected String getMethod() {
        return "GET";
    }

    @Override
    protected String getRelativeEndpoint() {
        return "/v1/payments/" + id;
    }

    @Override
    protected Payment parseResponse(InputStream response)
    {
        Payment payment = null;
        try
        {
            payment = SatisApi.Tools.JSON_MAPPER.readValue(response, Payment.class);
        }
        catch (IOException ex)
        {
            LOGGER.error(null, ex);
        }
        return payment;
    }

    public String getId()
    {
        return id;
    }
}
