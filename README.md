Mongbean is an alternative MongoDB (http://www.mongodb.org) driver for languages running on the Java Virtual Machine.

**Warning, Mongbean is in alpha state, and will probably kill some kittens.**

## Language support so far

### Java


#### Features

* insert + update + remove [API](http://github.com/jannehietamaki/mungbean/blob/master/mungbean-java/src/main/java/mungbean/DBCollection.java)
* Mongo documents can be mapped to generic java.util.collections (Maps + Lists) 
* Typesafe API for mapping Mongo documents to [POJOs](http://github.com/jannehietamaki/mungbean/blob/master/mungbean-java/src/test/java/mungbean/pojo/PojoWithIdIntegrationTest.java)
* Queries
* DSLs for conditional operators and updates
* Aggregation
* administration API (partial)


#### Code examples

    collection.update(new Query().field("foo").greaterThan(3), new Update().field("bar").increment(5)); // Find items having field 'foo' greater than 5 and update those field bar by 5.

    List<DomainObject> objects = collection.query(new Query().field("name").is("foo"));  // typesafe query of POJOs

    collection.query(Aggregation.distinct("foo", new Query().field("bar").greaterThan(5))); // select distinct values of field 'foo' of those items where value of 'bar' is greater than 5.

* More code examples can be found [here](http://github.com/jannehietamaki/mungbean/blob/master/mungbean-java/src/test/java/mungbean/MongoIntegrationTest.java)


#### Missing features

* GridFS, cursors
* Failover/replica pairs support is implemented but not yet ready for use

### Clojure

* Initial proof-of-concept done.
* Implemented features insert + update + remove + basic query
* [Working example](http://github.com/jannehietamaki/mungbean/blob/master/mungbean-clojure/test/mungbean_test.clj)

### Scala

Coming up next.

## Building

Just run ./boot.sh to fetch all dependencies and Leingingen binaries and after that ./build.sh can be used to build everything.

## License

ASL 2.0


