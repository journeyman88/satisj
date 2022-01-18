/*
 * Copyright 2022 journeyman.
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
package net.unknowndomain.satisj.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.unknowndomain.satisj.common.SatisJsonObject;

/**
 *
 * @author journeyman
 */
public class TestResults extends SatisJsonObject
{   
    public TestResults()
    {
        super(false);
    }
    
    @JsonProperty("authentication_key")
    private AuthenticationKey authKey;
    @JsonProperty("signature")
    private Signature signature;
    @JsonProperty("signed_string")
    private String signedString;

    public AuthenticationKey getAuthKey()
    {
        return authKey;
    }

    public void setAuthKey(AuthenticationKey authKey)
    {
        this.authKey = authKey;
    }

    public Signature getSignature()
    {
        return signature;
    }

    public void setSignature(Signature signature)
    {
        this.signature = signature;
    }

    public String getSignedString()
    {
        return signedString;
    }

    public void setSignedString(String signedString)
    {
        this.signedString = signedString;
    }
}
