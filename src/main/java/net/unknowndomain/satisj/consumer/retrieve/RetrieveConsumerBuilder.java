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

import net.unknowndomain.satisj.SatisCallBuilder;

/**
 *
 * @author journeyman
 */
public class RetrieveConsumerBuilder implements SatisCallBuilder<RetrieveConsumer>
{
    private String phoneNumber = "";
    
    
    public RetrieveConsumerBuilder matchUser(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
        return this;
    }
    
    @Override
    public RetrieveConsumer build()
    {
        return new RetrieveConsumer(phoneNumber);
    }
}
