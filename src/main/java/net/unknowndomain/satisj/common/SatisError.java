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
package net.unknowndomain.satisj.common;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author journeyman
 */
public class SatisError extends SatisJsonObject
{    
    @JsonProperty("code")
    private Long code;    
    @JsonProperty("message")
    private String message;
    
    public SatisError()
    {
        super(true);
    }

    public Long getCode()
    {
        return code;
    }

    public void setCode(Long code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
