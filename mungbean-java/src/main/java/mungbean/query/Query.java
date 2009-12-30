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

import java.util.LinkedHashMap;
import java.util.Map;

public class Query implements QueryBuilder {
	private final Map<String, Object> map = new LinkedHashMap<String, Object>();
	private final Map<String, Object> orderMap = new LinkedHashMap<String, Object>();
	private int skip;
	private int limit;

	public QueryField field(String key) {
		return new QueryField(this, map, orderMap, key);
	}

	public void setSkip(int skip) {
		this.skip = skip;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public Map<String, Object> build() {
		return map;
	}

	@Override
	public Map<String, Object> order() {
		return orderMap;
	}

	public int limit() {
		return limit;
	}

	public int skip() {
		return skip;
	}
}
