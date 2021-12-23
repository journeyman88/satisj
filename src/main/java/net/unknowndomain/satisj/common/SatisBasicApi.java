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

import net.unknowndomain.satisj.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import net.unknowndomain.satisj.auth.SatisAuth;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author journeyman
 */
public abstract class SatisBasicApi {
    protected final SatisAuth auth;
    protected final Environment env;
    protected final static Logger LOGGER = LoggerFactory.getLogger(SatisBasicApi.class);
    protected final static String USER_AGENT;
    protected String platformName;
    protected String platformVersion;
    protected String appName;
    protected String appVersion;
    protected String deviceType;
    protected String trackingCode;
    
    static {
        Properties props = new Properties();
        try (InputStream inStream = SatisBasicApi.class.getResourceAsStream("/net/unknowndomain/satisj/version.properties"))
        {
            props.load(inStream);
        } 
        catch (IOException ex)
        {
            LOGGER.error(null, ex);
        }
        USER_AGENT = props.getProperty("library.name", "StatisJ") + "/" + props.getProperty("library.version", "0.0.0");
    }
    
    public SatisBasicApi(Environment env, SatisAuth auth){
        this.auth = auth;
        this.env = env;
    }
    
    /**
     * Register a new currency and sets the right-shift to conver amount to units.
     * 
     * @param currencyCode
     * @param shift 
     */
    public static void registerCurrency(String currencyCode, int shift)
    {
        Tools.registerCurrency(currencyCode, shift);
    }
    
    public static class Tools
    {
        public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
        public static final FastDateFormat SIGN_DATE_FORMAT = FastDateFormat.getInstance("EEE, dd MMM yyyy HH:mm:ss Z");
        private static Map<String, Integer> CURRENCY_SHIFT = new HashMap<>();

        static
        {
            JSON_MAPPER.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            JSON_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            CURRENCY_SHIFT = new HashMap<>();
            registerCurrency("DEFAULT", 2);
            registerCurrency("EUR", 2);
        }
        
        private static void registerCurrency(String currencyCode, int shift)
        {
            if (currencyCode != null)
            {
                String curr = currencyCode.toUpperCase();
                Map<String, Integer> cs = new HashMap<>();
                cs.putAll(CURRENCY_SHIFT);
                cs.put(curr, shift);
                CURRENCY_SHIFT = Collections.unmodifiableMap(cs);
            }
        }

        public static Long getUnits(BigDecimal amount)
        {
            return getUnits(null, amount);
        }

        public static Long getUnits(String currencyCode, BigDecimal amount)
        {
            String curr = currencyCode;
            if ((curr == null) || (!CURRENCY_SHIFT.containsKey(curr)))
            {
                curr = "DEFAULT";
            }
            return amount.movePointRight(CURRENCY_SHIFT.get(curr)).longValue();
        }
    }

    public String getPlatformName()
    {
        return platformName;
    }

    public void setPlatformName(String platformName)
    {
        this.platformName = platformName;
    }

    public String getPlatformVersion()
    {
        return platformVersion;
    }

    public void setPlatformVersion(String platformVersion)
    {
        this.platformVersion = platformVersion;
    }

    public String getAppName()
    {
        return appName;
    }

    public void setAppName(String appName)
    {
        this.appName = appName;
    }

    public String getAppVersion()
    {
        return appVersion;
    }

    public void setAppVersion(String appVersion)
    {
        this.appVersion = appVersion;
    }

    public String getDeviceType()
    {
        return deviceType;
    }

    public void setDeviceType(String deviceType)
    {
        this.deviceType = deviceType;
    }

    public String getTrackingCode()
    {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode)
    {
        this.trackingCode = trackingCode;
    }
}
