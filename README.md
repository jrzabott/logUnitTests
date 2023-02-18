# logUnitTests
a quick approach/sample for unit testing logs using `SL4FJ` and `Logging` from `com.devexperts.logging.Logging`

Does something very simple:
1. Extends an `org.apache.logging.log4j.core.Appender`
2. Mocks an appender using Mockito
3. Get general context in use and add this custom appender (1 or 2)
4. Those appenders have associated Lists to store log events/messages so when can assert later.


This whole thing is very raw, but illustrates an issue that I had when I got a ticket related to logging, and I've been trying to use more TDD in my walk every single day.
