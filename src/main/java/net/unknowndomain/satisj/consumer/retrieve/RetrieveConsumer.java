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
package net.unknowndomain.satisj.consumer.retrieve;

import net.unknowndomain.satisj.SatisApiCall;
import net.unknowndomain.satisj.consumer.Consumer;
import okhttp3.Response;

/**
 *
 * @author journeyman
 */
public class RetrieveConsumer extends SatisApiCall<Consumer> {
    
    private final String phoneNumber;
    
    protected RetrieveConsumer(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }
    
    @Override
    protected String getBody() {
        return "";
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    @Override
    protected String getMethod() {
        return "GET";
    }

    @Override
    protected String getRelativeEndpoint() {
        return "/v1/consumers/"+ phoneNumber;
    }

    @Override
    protected Consumer parseResponse(Response response)
    {
        response.body();
        return null;
    }
    
}
