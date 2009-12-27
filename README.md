Mongbean is an alternative MongoDB (http://www.mongodb.org) driver for languages running on the Java Virtual Machine.

**Warning, Mongbean is in very alpha state, and will probably kill some kittens.**

Language support so far
-----------------------

**Java**

- Mongo documents can be mapped to generic java.util.collections (Maps + Lists) or to POJOs

- Missing features: Failover, GridFS, cursors, administration API, aggregation, conditional operators

- Implemented features insert + update + remove + basic query

**Clojure**

Initial proof-of-concept done.

- Implemented features insert + update + remove + basic query

- This works: http://github.com/jannehietamaki/mungbean/blob/master/mungbean-clojure/test/mungbean_test.clj

**Scala**

Coming up next.

Building
--------

Right now the Java part is built with sbt (doesn't automaticly run tests yet) and Clojure with Leiningen, so far there is nothing to get everything simply built.

License
-------
ASL 2.0


