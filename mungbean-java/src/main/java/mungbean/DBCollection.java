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

import mungbean.protocol.command.AbstractCommand;
import mungbean.protocol.command.Aggregation;
import mungbean.query.QueryBuilder;
import mungbean.query.UpdateBuilder;

public interface DBCollection<T> {
    CollectionAdmin collectionAdmin();

    String collectionName();

    T save(T doc);

    void update(ObjectId id, T doc);

    void update(QueryBuilder query, UpdateBuilder update);

    List<T> query(QueryBuilder query);

    <ResponseType> ResponseType query(Aggregation<ResponseType> aggregation, QueryBuilder query);

    T find(ObjectId id);

    void delete(ObjectId id);

    void delete(QueryBuilder query);

    <ResponseType> ResponseType command(AbstractCommand<ResponseType> command);

}