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
