/*
 * Copyright 2021 m.bignami.
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
package net.unknowndomain.satisj.async;

import java.io.IOException;
import java.nio.ByteBuffer;
import net.unknowndomain.satisj.common.SatisApi;
import net.unknowndomain.satisj.common.SatisApiException;
import net.unknowndomain.satisj.common.SatisError;
import net.unknowndomain.satisj.common.SatisJsonObject;
import org.apache.hc.client5.http.async.methods.AbstractBinResponseConsumer;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.util.ByteArrayBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author journeyman
 * @param <T>
 */
public class SatisJsonConsumer<T extends SatisJsonObject> extends AbstractBinResponseConsumer<T>
{
    private final static Logger LOGGER = LoggerFactory.getLogger(SatisJsonConsumer.class);
    private ByteArrayBuffer buffer = new ByteArrayBuffer(1024);
    private final Class<T> clazz;
    
    public SatisJsonConsumer(Class<T> clazz)
    {
        this.clazz = clazz;
    }

    @Override
    protected void start(HttpResponse response, ContentType contentType) throws HttpException, IOException
    {
    }

    @Override
    protected T buildResult()
    {
        try
        {
            return SatisApi.Tools.JSON_MAPPER.readValue(buffer.toByteArray(), clazz);
        } 
        catch (IOException ex)
        {
            try 
            {
                SatisError err = SatisApi.Tools.JSON_MAPPER.readValue(buffer.toByteArray(), SatisError.class);
                throw new SatisApiException(err);
            }
            catch (IOException e)
            {
                LOGGER.error(null, e);
            }
        }
        return null;
    }

    @Override
    protected int capacityIncrement()
    {
        return Integer.MAX_VALUE;
    }

    @Override
    protected void data(ByteBuffer src, boolean endOfStream) throws IOException
    {
        buffer.append(src);
    }

    @Override
    public void releaseResources()
    {
    }
}
