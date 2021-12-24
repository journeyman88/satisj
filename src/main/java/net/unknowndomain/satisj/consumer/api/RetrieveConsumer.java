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
package net.unknowndomain.satisj.consumer.api;

import net.unknowndomain.satisj.Environment;
import net.unknowndomain.satisj.common.SatisApi;
import net.unknowndomain.satisj.common.SatisApiCall;
import net.unknowndomain.satisj.consumer.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author journeyman
 */
public class RetrieveConsumer extends SatisApiCall<Consumer> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RetrieveConsumer.class);
    private final String phoneNumber;
    
    protected RetrieveConsumer(SatisApi api, String phoneNumber)
    {
        super(api, Consumer.class);
        this.phoneNumber = phoneNumber;
    }
    
    @Override
    public String getBody() {
        return "";
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    @Override
    public String getMethod() {
        return "GET";
    }

    @Override
    protected String getEndpoint(Environment env) {
        return env.getEndpoint().getPath() + "/v1/consumers/"+ phoneNumber;
    }
//
//    @Override
//    protected Consumer parseResponse(InputStream response)
//    {
//        Consumer consumer = null;
//        try
//        {
//            consumer = SatisApi.Tools.JSON_MAPPER.readValue(response, Consumer.class);
//        }
//        catch (IOException ex)
//        {
//            LOGGER.error(null, ex);
//        }
//        return consumer;
//    }
    
}
