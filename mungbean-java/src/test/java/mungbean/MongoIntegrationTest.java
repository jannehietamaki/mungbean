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

import org.junit.runner.RunWith;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

@RunWith(JDaveRunner.class)
public class MongoIntegrationTest extends Specification<DBCollection<Map<String, Object>>> {
	public class WithDatabase {
		public DBCollection<Map<String, Object>> create() {
			return new Mungbean("localhost", 27017).openDatabase("foobar").openCollection("foo");
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
			specify(context.command("getlasterror").get("n"), does.equal(0));
		}
	}
}
