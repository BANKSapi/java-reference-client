# BANKSapi Reference Client

This is the reference implementation of a BANKSapi client. This client
interfaces with the following BANKSapi APIs:
* Banks/Connect Customer API
* Banks/Connect Providers API
* Auth Management API
* Auth OAuth2 API

The client is written in Java 8 and uses Maven as its build system.
Dependencies to third party libraries have been minimized to ease integration.

The implementation is feature complete for all BANKSapi core features. Newer
features like background synchronization of banking accounts and REG/Shield
(our solution for tenants without a BaFin permission) will be included soon.

The generated Javadoc can be found at
https://docs.banksapi.de/javadoc/reference-client/.

# License

Copyright (c) 2018 BANKSapi GmbH

https://banksapi.de

The BANKSapi reference client implementation is licensed under the MIT License,
for details please see the provided LICENSE file.

