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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mungbean.protocol.command.Count;
import mungbean.protocol.command.Distinct;
import mungbean.protocol.command.Group;
import mungbean.protocol.command.LastError;

import org.junit.runner.RunWith;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

@RunWith(JDaveRunner.class)
public class MongoIntegrationTest extends Specification<Database> {
	public class WithDatabase {
		public Database create() {
			return new Mungbean("localhost", 27017).openDatabase(new ObjectId().toHex());
		}

		public void destroy() {
			context.dbAdmin().dropDatabase();
		}

		public void databaseCanBeAccessed() {
			DBCollection<Map<String, Object>> collection = context.openCollection("foo");
			long initialCount = collection.command(new Count());
			final ObjectId id = new ObjectId();
			collection.insert(new HashMap<String, Object>() {
				{
					put("foo", "bar");
					put("_id", id);
				}
			});
			specify(collection.command(new Count()), does.equal(initialCount + 1));
			HashMap<String, Object> idQuery = new HashMap<String, Object>() {
				{
					put("_id", id);
				}
			};
			List<Map<String, Object>> results = collection.query(idQuery, 0, 100);
			specify(results.size(), does.equal(1));
			specify(results.get(0).get("foo"), does.equal("bar"));
			collection.command(new Distinct("foo")).contains("bar");
			runGroup(collection);
			specify(collection.command(new Count(idQuery)), does.equal(1));
			collection.delete(idQuery);
			specify(collection.query(idQuery, 0, 100).size(), does.equal(0));
			specify(collection.command(new LastError()), does.equal(null));
			specify(collection.command(new Count()), does.equal(initialCount));
			specify(context.dbAdmin().getCollectionNames(), containsExactly("foo"));

		}

		private void runGroup(DBCollection<Map<String, Object>> collection) {
			HashMap<String, Double> initialValues = new HashMap<String, Double>();
			initialValues.put("foo", 0D);
			List<Map<String, Object>> result = collection.command(new Group(new String[] { "foo" }, initialValues, "function(obj, prev){ prev.csum=5; }"));
			specify(result.get(0).get("csum"), does.equal(5D));
		}
	}
}
