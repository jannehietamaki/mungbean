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

import java.util.List;
import java.util.Map;

public interface DBCollection<T> {

	T insert(T doc);

	void delete(Map<String, Object> query);

	T update(ObjectId id, T doc);

	void update(Map<String, Object> query, T doc, boolean upsert);

	List<T> query(Map<String, Object> rules, int first, int items);

	T find(ObjectId id);

	void delete(ObjectId id);

	Map<String, Object> command(String command);
}