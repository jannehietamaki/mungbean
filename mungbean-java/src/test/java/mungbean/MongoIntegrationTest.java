/*
   Copyright 2009 Janne Hietamäki

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package mungbean;

import static mungbean.CollectionUtil.map;
import static mungbean.CollectionUtil.merge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdave.Block;
import jdave.Specification;
import jdave.junit4.JDaveRunner;
import mungbean.protocol.command.Count;
import mungbean.protocol.command.Distinct;
import mungbean.protocol.command.Group;
import mungbean.protocol.command.admin.IndexOptionsBuilder;
import mungbean.query.Aggregation;
import mungbean.query.Query;
import mungbean.query.Update;

import org.junit.runner.RunWith;

@RunWith(JDaveRunner.class)
@SuppressWarnings("unchecked")
public class MongoIntegrationTest extends Specification<Database> {
	public class WithDatabase {
		final ObjectId id = new ObjectId();
		private final Map<String, Object> idQuery = map("_id", id);

		private final Map<String, Object> doc = newDoc(id, "bar");

		public Database create() {
			return new Mungbean("localhost", 27017).openDatabase(new ObjectId().toHex());
		}

		public void destroy() {
			context.dbAdmin().dropDatabase();
		}

		public void databaseTests() {
			DBCollection<Map<String, Object>> collection = context.openCollection("foo");
			long initialCount = collection.command(new Count());
			collection.save(doc);

			List<Map<String, Object>> results = collection.query(idQuery, 0, 100);
			specify(results.size(), does.equal(1));
			specify(results.get(0).get("foo"), does.equal("bar"));
			collection.command(new Distinct("foo")).contains("bar");
			specify(collection.command(new Count()), does.equal(initialCount + 1));
			runGroup(collection);
			specify(collection.command(new Count(idQuery)), does.equal(1));
			collection.delete(idQuery);
			specify(collection.query(idQuery, 0, 100).size(), does.equal(0));
			specify(context.dbAdmin().getCollectionNames(), containsExactly("foo"));
			specify(collection.command(new Count()), does.equal(initialCount));
		}

		private void runGroup(DBCollection<Map<String, Object>> collection) {
			HashMap<String, Double> initialValues = new HashMap<String, Double>();
			initialValues.put("foo", 0D);
			List<Map<String, Object>> result = collection.command(new Group(new String[] { "foo" }, initialValues, "function(obj, prev){ prev.csum=5; }"));
			specify(result.get(0).get("csum"), does.equal(5D));
		}

		public void violationOfUniqueIndexThrowsAnException() {
			final DBCollection<Map<String, Object>> collection = context.openCollection("foo");
			collection.collectionAdmin().ensureIndex(new String[] { "foo" }, new IndexOptionsBuilder().unique());
			collection.save(newDoc(new ObjectId(), "bar"));
			specify(new Block() {
				@Override
				public void run() throws Throwable {
					collection.save(doc);
				}
			}, does.raise(MongoException.class));
		}

		public void advancedQueriesCanBeDoneWithTheDsl() {
			final DBCollection<Map<String, Object>> collection = context.openCollection("foo");
			for (int a = 0; a < 10; a++) {
				collection.save(newDoc(new ObjectId(), a));
			}

			specify(collection.query(Aggregation.distinct("foo", new Query().field("foo").greaterThan(5))), containsExactly(6, 7, 8, 9));

			List<Map<String, Object>> values = collection.query(new Query().field("foo").greaterThan(3).lessThan(8));
			specify(values.size(), does.equal(4));
		}

		public void updatesCanBeDoneWithTheDsl() {
			final DBCollection<Map<String, Object>> collection = context.openCollection("foo");
			for (int a = 0; a < 10; a++) {
				collection.save(newDoc(new ObjectId(), a));
			}

			collection.update(new Query().field("foo").greaterThan(3), new Update().field("foo").increment(5));
			specify(collection.query(new Query().field("foo").is(9)).size(), does.equal(2));
		}
	}

	private Map<String, Object> newDoc(final ObjectId id, final Object value) {
		return merge(map("foo", value), map("_id", id));
	}
}
