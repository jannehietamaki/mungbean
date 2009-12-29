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

package mungbean.protocol.command;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mungbean.DBCollection;
import mungbean.query.QueryBuilder;

public class Distinct extends Command<List<Object>> {
	private final Map<String, Object> query;
	private final String field;

	public Distinct(String field) {
		this.query = Collections.emptyMap();
		this.field = field;
	}

	public Distinct(String field, Map<String, Object> query) {
		this.query = query;
		this.field = field;
	}

	public Distinct(String field, QueryBuilder query) {
		this.query = query.build();
		this.field = field;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> parseResponse(Map<String, Object> values) {
		return (List<Object>) values.get("values");
	}

	@Override
	public Map<String, Object> toMap(DBCollection<?> collection) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("distinct", collection.collectionName());
		map.put("key", field);
		map.put("query", query);
		return map;
	}
}
