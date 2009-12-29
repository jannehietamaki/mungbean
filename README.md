Mongbean is an alternative MongoDB (http://www.mongodb.org) driver for languages running on the Java Virtual Machine.

**Warning, Mongbean is in alpha state, and will probably kill some kittens.**

Language support so far
-----------------------

**Java**

- Mongo documents can be mapped to generic java.util.collections (Maps + Lists) or to [POJOs](http://github.com/jannehietamaki/mungbean/blob/master/mungbean-java/src/test/java/mungbean/pojo/PojoWithIdIntegrationTest.java)

- Missing features: GridFS, cursors

* Implemented features 
- insert + update + remove [API](http://github.com/jannehietamaki/mungbean/blob/master/mungbean-java/src/main/java/mungbean/DBCollection.java)
- basic query 
- DSLs for aggregation commands and conditional operators 
- administration API (partial)

- Failover/replica pairs is implemented but not yet ready for use

- Code examples can be found [here: ](http://github.com/jannehietamaki/mungbean/blob/master/mungbean-java/src/test/java/mungbean/MongoIntegrationTest.java)

Update:

    collection.update(new Query().field("foo").greaterThan(3), new Update().field("foo").increment(5));

Query POJOs with the query DSL:

    List<DomainObject> objects = collection.query(new Query().field("name").is("foo");

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


