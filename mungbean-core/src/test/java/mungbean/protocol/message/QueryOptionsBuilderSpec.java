package com.mongodb.protocol.message;

import org.junit.runner.RunWith;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

@RunWith(JDaveRunner.class)
public class QueryOptionsBuilderSpec extends Specification<QueryOptionsBuilder> {
	public class WithNoOptionsSet {
		public QueryOptionsBuilder create() {
			return new QueryOptionsBuilder();
		}

		public void returnedValueIsValid() {
			specify(context.build(), does.equal(0));
		}
	}

	public class WithSomeOptionsSet {
		public QueryOptionsBuilder create() {
			return new QueryOptionsBuilder().noCursorTimeout().slaveOk();
		}

		public void returnedValueIsValid() {
			specify(context.build(), does.equal(16 + 4));
		}
	}
}
