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

import mungbean.protocol.command.AbstractCommand;
import mungbean.query.AggregationBuilder;
import mungbean.query.QueryBuilder;
import mungbean.query.UpdateBuilder;

public interface DBCollection<T> {
	CollectionAdmin collectionAdmin();

	String collectionName();

	T save(T doc);

	void update(ObjectId id, T doc);

	void update(ObjectId id, UpdateBuilder update);

	void update(Map<String, Object> query, Map<String, Object> updates);

	void update(QueryBuilder query, UpdateBuilder update);

	List<T> query(Map<String, Object> rules, int first, int items);

	List<T> query(Map<String, Object> rules, Map<String, Object> order, int first, int items);

	List<T> query(QueryBuilder query);

	<ResponseType> ResponseType query(AggregationBuilder<ResponseType> builder);

	T find(ObjectId id);

	void delete(ObjectId id);

	void delete(QueryBuilder query);

	void delete(Map<String, Object> query);

	<ResponseType> ResponseType command(AbstractCommand<ResponseType> command);

}