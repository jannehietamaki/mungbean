Mongbean is an alternative MongoDB (http://www.mongodb.org) driver for languages running on the Java Virtual Machine.

**Warning, Mongbean is in alpha state, and will probably kill some kittens.**

Language support so far
-----------------------

**Java**

- Mongo documents can be mapped to generic java.util.collections (Maps + Lists) or to POJOs

- Missing features: GridFS, cursors

- Implemented features insert + update + remove + basic query + aggregation commands + conditional operators + administration API (partial)

- Failover/replica pairs is implemented but not yet ready for use

- Code example can be found from here: http://github.com/jannehietamaki/mungbean/blob/master/mungbean-java/src/test/java/mungbean/MongoIntegrationTest.java

**Clojure**

- Initial proof-of-concept done.

- Implemented features insert + update + remove + basic query

- This works: http://github.com/jannehietamaki/mungbean/blob/master/mungbean-clojure/test/mungbean_test.clj

**Scala**

Coming up next.

Building
--------

Just run ./boot.sh to fetch all dependencies and Leingingen binaries and after that ./build.sh can be used to build everything.

License
-------
ASL 2.0


