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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mungbean.util.PojoMapper;

public class PojoDBCollection<T> implements DBCollection<T> {

	private final DBCollection<Map<String, Object>> storage;
	private final PojoMapper<T> mapper;

	public PojoDBCollection(DBCollection<Map<String, Object>> storage, Class<T> typeClass) {
		this.storage = storage;
		mapper = new PojoMapper<T>(typeClass);
	}

	@Override
	public void delete(Map<String, Object> query) {
		storage.delete(query);
	}

	@Override
	public void delete(ObjectId id) {
		storage.delete(id);
	}

	@Override
	public T find(ObjectId id) {
		return mapper.fromMap(storage.find(id));
	}

	@Override
	public void insert(T doc) {
		storage.insert(mapper.toMap(doc));
	}

	@Override
	public List<T> query(Map<String, Object> rules, int first, int items) {
		List<T> result = new ArrayList<T>();
		List<Map<String, Object>> docs = storage.query(rules, first, items);
		for (Map<String, Object> doc : docs) {
			result.add(mapper.fromMap(doc));
		}
		return result;
	}

	@Override
	public void update(Map<String, Object> query, T doc, boolean upsert) {
		storage.update(query, mapper.toMap(doc), upsert);
	}

	@Override
	public Map<String, Object> command(String command) {
		return storage.command(command);
	}

}
