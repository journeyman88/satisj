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
package net.unknowndomain.satisj.auth;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import net.unknowndomain.satisj.Environment;
import net.unknowndomain.satisj.common.SatisApi;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author journeyman
 */
public class SatisAuth {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SatisAuth.class);
    
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    
    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final String keyId;
    
    private SatisAuth(PrivateKey privateKey, PublicKey publicKey, String keyId)
    {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.keyId = keyId;
    }
    
    private static String keyToPem(Object key) throws IOException
    {
        StringWriter sw = new StringWriter();
        JcaPEMWriter pemWriter = new JcaPEMWriter(sw);
        pemWriter.writeObject(key);
        pemWriter.flush();
        return sw.toString();
    }
    
    private static Object pemToKey(String pemData) throws IOException
    {
        StringReader sr = new StringReader(pemData);
        PEMParser pemParser = new PEMParser(sr);
        return pemParser.readObject();
    }
    
    private static PrivateKey pemToPrivKey(String pemData) throws IOException
    {
        Object pemObj = pemToKey(pemData);
        if (pemObj instanceof PrivateKey)
        {
            return (PrivateKey) pemObj;
        }
        if (pemObj instanceof PEMKeyPair)
        {
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            return converter.getPrivateKey(((PEMKeyPair) pemObj).getPrivateKeyInfo());
        }
        if (pemObj instanceof KeyPair)
        {
            return ((KeyPair) pemObj).getPrivate();
        }
        return null;
    }
    
    private static PublicKey pemToPubKey(String pemData) throws IOException
    {
        Object pemObj = pemToKey(pemData);
        if (pemObj instanceof PublicKey)
        {
            return (PublicKey) pemObj;
        }
        if (pemObj instanceof PEMKeyPair)
        {
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            return converter.getPublicKey(((PEMKeyPair) pemObj).getPublicKeyInfo());
        }
        if (pemObj instanceof KeyPair)
        {
            return ((KeyPair) pemObj).getPublic();
        }
        return null;
    }
    
    public void saveToDir(Path directory) throws SatisAuthException
    {
        if (directory == null)
        {
            throw new SatisAuthException("Path is null");
        }
        try 
        {
            if (Files.notExists(directory))
            {
                Files.createDirectories(directory);
            }
            if (!Files.isDirectory(directory))
            {
                throw new SatisAuthException("Path is not directory");
            }
            if (!Files.isWritable(directory))
            {
                throw new SatisAuthException("Path is not writable");
            }
            Path pubKey = directory.resolve("pubKey.pem");
            Path privKey = directory.resolve("privKey.pem");
            Path keyTxt = directory.resolve("keyId.txt");
            Files.write(pubKey, keyToPem(publicKey).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            Files.write(privKey, keyToPem(privateKey).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            Files.write(keyTxt, keyId.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
        catch (IOException ex)
        {
            LOGGER.error(null, ex);
            throw new SatisAuthException("Cannot save");
        }
    }
    
    public void saveToProperties(Properties props) throws SatisAuthException
    {
        if (props == null)
        {
            throw new SatisAuthException("Properties is null");
        }
        try 
        {
            props.setProperty("net.unknowndomain.satisj.privateKey", keyToPem(privateKey));
            props.setProperty("net.unknowndomain.satisj.publicKey", keyToPem(publicKey));
            props.setProperty("net.unknowndomain.satisj.keyId", keyId);
        }
        catch (IOException ex)
        {
            LOGGER.error(null, ex);
            throw new SatisAuthException("Cannot save");
        }
    }
    
    public static SatisAuth loadAuth(Path directory) throws SatisAuthException
    {
        if (directory == null)
        {
            throw new SatisAuthException("Path is null");
        }
        if (!Files.isDirectory(directory))
        {
            throw new SatisAuthException("Path is not directory");
        }
        if (!Files.isReadable(directory))
        {
            throw new SatisAuthException("Path is not readable");
        }
        Path pubPem = directory.resolve("pubKey.pem");
        Path privPem = directory.resolve("privKey.pem");
        Path keyTxt = directory.resolve("keyId.txt");
        try 
        {
            PrivateKey privKey = pemToPrivKey(new String(Files.readAllBytes(privPem), StandardCharsets.UTF_8));
            PublicKey pubKey = pemToPubKey(new String(Files.readAllBytes(pubPem), StandardCharsets.UTF_8));
            String keyId = new String(Files.readAllBytes(keyTxt), StandardCharsets.UTF_8);
            return new SatisAuth(privKey, pubKey, keyId);
        }
        catch (IOException ex)
        {
            LOGGER.error(null, ex);
            throw new SatisAuthException("Cannot save");
        }
    }
    
    public static SatisAuth loadAuth(Properties props) throws SatisAuthException
    {
        if (props == null)
        {
            throw new SatisAuthException("Properties is null");
        }
        try 
        {
            PrivateKey privKey = pemToPrivKey(props.getProperty("net.unknowndomain.satisj.privateKey"));
            PublicKey pubKey = pemToPubKey(props.getProperty("net.unknowndomain.satisj.publicKey"));
            String keyId = props.getProperty("net.unknowndomain.satisj.keyId");
            return new SatisAuth(privKey, pubKey, keyId);
        }
        catch (IOException ex)
        {
            LOGGER.error(null, ex);
            throw new SatisAuthException("Cannot save");
        }
    }
    
    public static SatisAuth generateAuth(Environment env, String authToken) throws SatisAuthException
    {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()){
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(4096);
            KeyPair key = keyGen.generateKeyPair();
            PrivateKey priv = key.getPrivate();
            PublicKey pub = key.getPublic();
            Map<String, String> params = new HashMap<>();
            params.put("public_key", keyToPem(pub));
            params.put("token", authToken);
            HttpPost req = new HttpPost(env.getEndpoint() + "/v1/authentication_keys");
            HttpEntity body = new StringEntity(SatisApi.Tools.JSON_MAPPER.writeValueAsString(params), ContentType.APPLICATION_JSON.withCharset(StandardCharsets.UTF_8));
            req.setEntity(body);
            ClassicHttpResponse resp = httpClient.execute(req);
            switch(resp.getCode())
            {
                case 400:
                    throw new SatisAuthException("Invalid RSA Key");
                case 403:
                    throw new SatisAuthException("Token already paired");
                case 404:
                    throw new SatisAuthException("Device token not found");
            }
            Map keyWrapper = SatisApi.Tools.JSON_MAPPER.readValue(resp.getEntity().getContent(), HashMap.class);
            return new SatisAuth(priv, pub, (String) keyWrapper.get("key_id"));
        } 
        catch (NoSuchAlgorithmException | IOException ex) 
        {
            LOGGER.error(null, ex);
            throw new SatisAuthException();
        }
        
    }
    
    public PrivateKey getPrivateKey() {
        return privateKey;
    }
    
    public PublicKey getPublicKey() {
        return publicKey;
    }
    
    public String getKeyId() {
        return keyId;
    }
}
