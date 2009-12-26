package com.mongodb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.runner.RunWith;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

@RunWith(JDaveRunner.class)
public class MongoIntegrationTest extends Specification<DBCollection> {
	public class WithDatabase {
		public DBCollection create() {
			return new Mongo("localhost", 27017).openDatabase("foobar").openCollection("foo");
		}

		public void databaseCanBeAccessed() {
			final ObjectId id = new ObjectId();
			context.insert(new HashMap<String, Object>() {
				{
					put("foo", "bar");
					put("_id", id);
				}
			});
			HashMap<String, Object> idQuery = new HashMap<String, Object>() {
				{
					put("_id", id);
				}
			};
			List<Map<String, Object>> results = context.query(idQuery, 0, 100);
			specify(results.size(), does.equal(1));
			specify(results.get(0).get("foo"), does.equal("bar"));
			context.delete(idQuery);
			specify(context.query(idQuery, 0, 100).size(), does.equal(0));
		}
	}
}
