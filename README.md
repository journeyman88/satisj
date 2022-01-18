# satisj
A Java Wrapper for SatisPay API

This wrapper should be able to:
 - Create a key-pair and register it on Satispay to use it as authentication on other API calls. (SatisAuth.generateAuth)
 - Load a previously stored key-pair from a directory or a Properties object to use it on API calls. (SatisAuth.loadAuth)
 - Create an api client for synchronized calls (SatisSimpleClient)
 - Use an API client to work on the Satispay API (SatisApi.consumer(), SatisApi.payment(), SatisApi.authorization())

There's experimental code to implement an Async client (SatisAsyncClient) but is not working right now. Also there's plan to build a Reactive client.
