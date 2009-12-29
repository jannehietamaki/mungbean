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

import jdave.Block;
import jdave.Specification;
import jdave.junit4.JDaveRunner;
import mungbean.DBCollection;
import mungbean.Database;
import mungbean.Mungbean;
import mungbean.NotFoundException;
import mungbean.ObjectId;
import mungbean.TestObjectWithId;
import mungbean.query.Query;

import org.junit.runner.RunWith;

@RunWith(JDaveRunner.class)
public class PojoWithIdIntegrationTest extends Specification<Database> {
	public class WithDatabase {
		public Database create() {
			return new Mungbean("localhost", 27017).openDatabase(new ObjectId().toHex());
		}

		public void destroy() {
			context.dbAdmin().dropDatabase();
		}

		public void objectWithIdCanBeStored() {
			final DBCollection<TestObjectWithId> collection = context.openCollection("foo", TestObjectWithId.class);
			final TestObjectWithId item = collection.insert(new TestObjectWithId("foo", 123));
			TestObjectWithId itemFromDb = collection.find(item.id());

			specify(collection.query(new Query().field("name").is("foo")).get(0).value(), does.equal(123));

			specify(itemFromDb.id(), does.equal(item.id()));
			collection.delete(item.id());
			specify(new Block() {
				@Override
				public void run() throws Throwable {
					collection.find(item.id());
				}
			}, does.raise(NotFoundException.class));
		}
	}
}
