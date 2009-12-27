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
package mungbean.protocol.message;

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
