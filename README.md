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
The generated KeyPair will use the RSA algorithm with a key length of 4096 bit. At the moment of writing this is a fixed value.

```java
SatisAuth auth = SatisAuth.generateAuth(Environment.STAGING, "myAuthToken");
```

### Store a Keypair
SatisJ can store a created KeyPair, alongside its KeyID, for future usage once created.

```java
SatisAuth auth = SatisAuth.generateAuth(Environment.STAGING, "myAuthToken");
Properties authStoreProps = new Properties();
Path authStoreDir = Paths.get("/path/to/authStore/");
auth.saveToProperties(authStoreProps);
auth.saveToDir(authStoreDir);
```

### Load a Keypair
SatisJ can load a previously created KeyPair, alongside its KeyID, to be used on API calls.

To load a previously stored KeyPair you shoul call the "SatisAuth.loadAuth" static method, to which should be passed a Properties object or a Path object.

```java
SatisAuth authFromProps = SatisAuth.loadAuth(authStoreProps);
SatisAuth authFromDir = SatisAuth.loadAuth(authStoreDir);
```

## Client
Once you have a valid SatisAuth object you can create a SatisApi client to interface yourself with the APIs.
Currently there are 2 implementations of the client: SatisSimpleClient which internally uses Apache HttpCore in synchronous mode and an experimental SatisAsyncClient which uses the HttpCore async api.

```java
SatisApi client = new SatisSimpleClient(Environment.STAGING, auth);
```

From the client you can obtain the builders from which the calls are handled.
The model adopted by SatisJ - from which any API operation works - is one decribed by "Builder -> Call -> Result":
 - You use a SatisCallBuilder to configure the call parameters and build a SatisApiCall object.
 - The SatisApiCall is unmodifiable and has an IdempotencyKey associated, to allow the server to correctly handle duplicate operation.
 - The operation described by SatisApiCall can be launched (even several times) using one of three possibile modes:
   - execute(): Synchronus mode: result in a SatisJsonObject.
   - queue(): Asynchronus mode: result in a Future<SatisJsonObject>.
   - call(): Reactive mode: result in an Observable<SatisJsonObject>.

### Customer API
On this API endpoint only the retrive operation is available. 
```java
RetrieveConsumerBuilder retriveOpBuilder = client.consumer().retrieve();
retriveOpBuilder.phoneNumber("+390000000000");
RetrieveConsumer retriveOp = retriveOpBuilder.build();
Consumer consumerSync = retriveOp.execute();
Future<Consumer> consumerFuture = retriveOp.queue();
Observable<Consumer> consumerObs = retriveOp.call();
```

### Authorization API

### Payment API