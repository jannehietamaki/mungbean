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
package mungbean.util;

import java.util.HashMap;
import java.util.Map;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import mungbean.TestObject;

import org.junit.runner.RunWith;

@RunWith(JDaveRunner.class)
public class PojoMapperSpec extends Specification<PojoMapper<TestObject>> {
	public class WithValidObject {
		public PojoMapper<TestObject> create() {
			return new PojoMapper<TestObject>(TestObject.class);
		}

		public void objectCanBeMappedToMap() {
			Map<String, Object> map = context.toMap(new TestObject("foo", 123));
			specify(map.get("name"), does.equal("foo"));
			specify(map.get("value"), does.equal(123));
			specify(map.size(), does.equal(2));
		}

		public void mapCanBeMappedToObject() {
			Map<String, Object> map = new HashMap<String, Object>() {
				{
					put("name", "foo");
					put("value", 123);
				}
			};
			TestObject obj = context.fromMap(map);
			specify(obj.name(), does.equal("foo"));
			specify(obj.value(), does.equal(123));
		}
	}
}
