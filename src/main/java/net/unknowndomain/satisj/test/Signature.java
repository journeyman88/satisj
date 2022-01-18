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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 *
 * @author journeyman
 */
@JsonIgnoreProperties
public class Signature
{
    @JsonProperty("key_id")
    private String keyId;
    @JsonProperty("algorithm")
    private String algorithm;
    @JsonProperty("headers")
    private List<String> headers;
    @JsonProperty("signature")
    private String signature;
    @JsonProperty("resign_required")
    private Boolean resignRequired;
    @JsonProperty("iteration_count")
    private Integer iterationCount;
    @JsonProperty("valid")
    private Boolean valid;

    public String getKeyId()
    {
        return keyId;
    }

    public void setKeyId(String keyId)
    {
        this.keyId = keyId;
    }

    public String getAlgorithm()
    {
        return algorithm;
    }

    public void setAlgorithm(String algorithm)
    {
        this.algorithm = algorithm;
    }

    public List<String> getHeaders()
    {
        return headers;
    }

    public void setHeaders(List<String> headers)
    {
        this.headers = headers;
    }

    public String getSignature()
    {
        return signature;
    }

    public void setSignature(String signature)
    {
        this.signature = signature;
    }

    public Boolean getResignRequired()
    {
        return resignRequired;
    }

    public void setResignRequired(Boolean resignRequired)
    {
        this.resignRequired = resignRequired;
    }

    public Integer getIterationCount()
    {
        return iterationCount;
    }

    public void setIterationCount(Integer iterationCount)
    {
        this.iterationCount = iterationCount;
    }

    public Boolean getValid()
    {
        return valid;
    }

    public void setValid(Boolean valid)
    {
        this.valid = valid;
    }
}
