package mungbean;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

@RunWith(JDaveRunner.class)
public class PojoIntegrationTest extends Specification<DBCollection<TestObject>> {
	public class WithDatabase {
		public DBCollection<TestObject> create() {
			return new Mungbean("localhost", 27017).openDatabase("foobar").openCollection("foo", TestObject.class);
		}

		public void objectWithoutIdCanBeStored() {
			context.insert(new TestObject("foo", 123));
		}

	}
}
