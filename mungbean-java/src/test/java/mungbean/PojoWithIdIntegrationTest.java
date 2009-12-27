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

import jdave.Block;
import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

@RunWith(JDaveRunner.class)
public class PojoWithIdIntegrationTest extends Specification<DBCollection<TestObjectWithId>> {
	public class WithDatabase {
		public DBCollection<TestObjectWithId> create() {
			return new Mungbean("localhost", 27017).openDatabase("foobar").openCollection("foo", TestObjectWithId.class);
		}

		public void objectWithIdCanBeStored() {
			final TestObjectWithId item = new TestObjectWithId("foo", 123);
			context.insert(item);
			TestObjectWithId itemFromDb = context.find(item.id());
			specify(itemFromDb.id(), does.equal(item.id()));
			context.delete(item.id());
			specify(new Block() {
				@Override
				public void run() throws Throwable {
					context.find(item.id());
				}
			}, does.raise(NotFoundException.class));
		}
	}
}
