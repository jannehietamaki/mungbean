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

import java.util.Map;
import java.util.regex.Pattern;

import mungbean.protocol.bson.Code;

import scala.actors.threadpool.Arrays;
import static mungbean.CollectionUtil.map;

public class QueryField {
	Map<String, Object> result;
	String key;

	QueryField(Map<String, Object> result, String key) {
		this.result = result;
		this.key = key;
	}

	public QueryField is(Object value) {
		result.put(key, value);
		return this;
	}

	public QueryField matches(Pattern pattern) {
		return is(pattern);
	}

	public QueryField lessThan(Object value) {
		put(key, map("$lt", value));
		return this;
	}

	public QueryField greaterThan(Object value) {
		put(key, map("$gt", value));
		return this;
	}

	public QueryField lessOrEqual(Object value) {
		put(key, map("$lte", value));
		return this;
	}

	public QueryField greaterOrEqual(Object value) {
		put(key, map("$gte", value));
		return this;
	}

	public QueryField not(Object value) {
		put(key, map("$ne", value));
		return this;
	}

	public QueryField in(Object... values) {
		put(key, map("$in", Arrays.asList(values)));
		return this;
	}

	public QueryField notIn(Object... values) {
		put(key, map("$nin", Arrays.asList(values)));
		return this;
	}

	public QueryField all(Object... values) {
		put(key, map("$all", Arrays.asList(values)));
		return this;
	}

	public QueryField size(int value) {
		put(key, map("$size", value));
		return this;
	}

	public QueryField exists(boolean value) {
		put(key, map("$exists", value));
		return this;
	}

	public QueryField where(String script) {
		result.put(key, new Code(script));
		return this;
	}

	@SuppressWarnings("unchecked")
	private void put(String key, Map<String, Object> value) {
		Object val = result.get(key);
		if (val != null) {
			if (!(val instanceof Map)) {
				throw new IllegalArgumentException("Invalid combination of arguments");
			}
			Map<String, Object> valueMap = (Map<String, Object>) val;
			valueMap.putAll(value);
		} else {
			result.put(key, value);
		}
	}

}
