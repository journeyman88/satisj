# satisj
A Java Wrapper for SatisPay API

This wrapper is usable to:
 - Create a key-pair and register it on Satispay to use it as authentication on other API calls.
 - Load a previously stored KeyPair from a directory or a Properties object to use it on API calls.
 - Create an api client.
 - Use an API client to work on the Satispay API.

## Authentication
SatisPay uses the Signing HTTP Messages internet draft as its security foundation. 
This is handled by the SatisAuth class in SatisJ, which can be used to register a new KeyPair, to load an existing KeyPair, and - by the client class - to sign every call.

### Create and Register a KeyPair
SatisJ is able to create a secure KeyPair to identify the user and register them thru the SatisPay environment.

To create a new KeyPair simply use the static method "SatisAuth.generateAuth" selecting the correct environment ("STAGING" or "PRODUCTION") and the authToken associated with your business account.
The generated KeyPair will be the RSA algorithm with a key length of 4096 bit. At the moment of writing this is a fixed value.

```java
    SatisAuth auth = SatisAuth.generateAuth(Environment.STAGING, "myAuthToken");
```

### Store a Keypair
SatisJ can store a created KeyPair, alongside its KeyID, for future usage once created.

### Load a Keypair
SatisJ can load a previously created KeyPair, alongside its KeyID, to be used on API calls.

To load a previously stored KeyPair you shoul call the "SatisAuth.loadAuth" static method, to which should be passed a Properties object or a Path object.

