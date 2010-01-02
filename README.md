Mongbean is an alternative MongoDB (http://www.mongodb.org) driver for languages running on the Java Virtual Machine.

## Language support so far

### Java


#### Features

* Basic operations: query, save, update, remove, ensureIndex, findOne, limit, sort [API](http://github.com/jannehietamaki/mungbean/blob/master/mungbean-java/src/main/java/mungbean/DBCollection.java)
* Mongo documents can be mapped to generic java.util.collections (Maps + Lists) 
* Typesafe [API](http://github.com/jannehietamaki/mungbean/blob/master/mungbean-java/src/test/java/mungbean/pojo/PojoWithIdIntegrationTest.java) for mapping POJOs to Mongo documents and back
* DSLs for conditional operators, sorting, aggregation and updates
* administration API (partial), authentication

#### Code examples

    // Find items having field 'foo' greater than 5 and update those field 'bar' by 5.
    collection.update(new Query().field("foo").greaterThan(3), new Update().field("bar").increment(5)); 

    // typesafe query of POJOs
    List<DomainObject> objects = collection.query(new Query().field("name").is("foo"));

    // Sorting
    List<DomainObject> objects = collection.query(new Query().field("name").orderAscending());

    // select distinct values of field 'foo' of those items where value of 'bar' is greater than 5.
    collection.query(Aggregation.distinct("foo", new Query().field("bar").greaterThan(5))); 

* More code examples can be found [here](http://github.com/jannehietamaki/mungbean/blob/master/mungbean-java/src/test/java/mungbean/MongoIntegrationTest.java)

#### Missing features

* GridFS, cursors
* Failover/replica pairs support is implemented but not quite yet ready for real use.

### Clojure

* Basic operations: query, save, update, remove, limit, sort, aggregation
* [Working example](http://github.com/jannehietamaki/mungbean/blob/master/mungbean-clojure/test/mungbean_test.clj)

### Scala

Coming up next.

## Building

Just run ./boot.sh to fetch all dependencies and Leingingen binaries and after that ./build.sh can be used to build everything.

## License

ASL 2.0


