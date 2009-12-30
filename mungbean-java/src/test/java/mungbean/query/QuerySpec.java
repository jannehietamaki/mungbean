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

package mungbean.query;

import static mungbean.CollectionUtil.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import mungbean.protocol.bson.Code;

import org.junit.runner.RunWith;

@RunWith(JDaveRunner.class)
public class QuerySpec extends Specification<Query> {
	public class WithSimpleQuery {
		public Query create() {
			Query builder = new Query();
			builder.field("foo").is("bar");
			return builder;
		}

		public void queryMapCanBeConstructed() {
			specify(context.build(), does.equal(map("foo", "bar")));
		}
	}

	public class WithConditionalOperators {
		public Query create() {
			Query builder = new Query();
			builder.field("foo").lessThan(5).greaterThan(1);
			builder.field("bar").lessOrEqual(5);
			builder.field("bar").greaterOrEqual(2);
			return builder;
		}

		public void queryMapCanBeConstructed() {
			specify(context.build(), does.equal(new HashMap<String, Object>() {
				{
					put("foo", new HashMap<String, Object>() {
						{
							put("$lt", 5);
							put("$gt", 1);
						}
					});
					put("bar", new HashMap<String, Object>() {
						{
							put("$lte", 5);
							put("$gte", 2);
						}
					});
				}
			}));
		}
	}

	public class WithNot {
		public Query create() {
			Query builder = new Query();
			builder.field("foo").not("bar");
			return builder;
		}

		public void queryMapCanBeConstructed() {
			specify(context.build(), does.equal(map("foo", map("$ne", "bar"))));
		}
	}

	public class WithIn {
		public Query create() {
			Query builder = new Query();
			builder.field("foo").in("foo", "bar", "zoo");
			return builder;
		}

		public void queryMapCanBeConstructed() {
			final List<String> values = new ArrayList<String>();
			values.add("foo");
			values.add("bar");
			values.add("zoo");
			specify(context.build(), does.equal(map("foo", map("$in", values))));
		}
	}

	public class WithNotIn {
		public Query create() {
			Query builder = new Query();
			builder.field("foo").notIn("foo", "bar", "zoo");
			return builder;
		}

		public void queryMapCanBeConstructed() {
			final List<String> values = new ArrayList<String>();
			values.add("foo");
			values.add("bar");
			values.add("zoo");
			specify(context.build(), does.equal(map("foo", map("$nin", values))));
		}
	}

	public class WithAllInArray {
		public Query create() {
			Query builder = new Query();
			builder.field("foo").all("foo", "bar", "zoo");
			return builder;
		}

		public void queryMapCanBeConstructed() {
			final List<String> values = new ArrayList<String>();
			values.add("foo");
			values.add("bar");
			values.add("zoo");
			specify(context.build(), does.equal(map("foo", map("$all", values))));
		}
	}

	public class WithArraySize {
		public Query create() {
			Query builder = new Query();
			builder.field("foo").size(5);
			return builder;
		}

		public void queryMapCanBeConstructed() {
			specify(context.build(), does.equal(map("foo", map("$size", 5))));
		}
	}

	public class WithFieldExists {
		public Query create() {
			Query builder = new Query();
			builder.field("foo").exists(true);
			return builder;
		}

		public void queryMapCanBeConstructed() {
			specify(context.build(), does.equal(map("foo", map("$exists", true))));
		}
	}

	public class WithPattern {
		Pattern pattern = Pattern.compile("foo(.*)");

		public Query create() {
			Query builder = new Query();
			builder.field("foo").matches(pattern);
			return builder;
		}

		public void queryMapCanBeConstructed() {
			specify(context.build(), does.equal(map("foo", pattern)));
		}
	}

	public class WithWhere {

		public Query create() {
			Query builder = new Query();
			builder.field("foo").where("this.a>3");
			return builder;
		}

		public void queryMapCanBeConstructed() {
			specify(context.build(), does.equal(map("foo", new Code("this.a>3"))));
		}
	}
}
