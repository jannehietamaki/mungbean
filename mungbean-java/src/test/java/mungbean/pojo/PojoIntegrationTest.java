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
package mungbean.pojo;

import java.util.List;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import mungbean.DBCollection;
import mungbean.Database;
import mungbean.Mungbean;
import mungbean.ObjectId;
import mungbean.TestObject;
import mungbean.query.Query;

import org.junit.runner.RunWith;

@RunWith(JDaveRunner.class)
public class PojoIntegrationTest extends Specification<Database> {
	public class WithDatabase {
		public Database create() {
			return new Mungbean("localhost", 27017).openDatabase(new ObjectId().toHex());
		}

		public void destroy() {
			context.dbAdmin().dropDatabase();
		}

		public void objectWithoutIdCanBeStoredAndRetrieved() {
			DBCollection<TestObject> collection = context.openCollection("foo", TestObject.class);
			collection.save(new TestObject("foo", 123));
			List<TestObject> objs = collection.query(new Query().field("name").is("foo"));
			specify(objs.size(), does.equal(1));
			specify(objs.get(0).name(), does.equal("foo"));
		}
	}
}
