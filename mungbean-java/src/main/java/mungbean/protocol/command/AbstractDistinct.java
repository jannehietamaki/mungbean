/*
   Copyright 2009-2010 Janne Hietamäki

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

import java.util.LinkedHashMap;
import java.util.Map;

import mungbean.DBCollection;
import mungbean.query.QueryBuilder;

public abstract class AbstractDistinct<ResponseType> extends Aggregation<ResponseType> {
    private final String field;

    public AbstractDistinct(String field) {
        this.field = field;
    }

    @Override
    public Map<String, Object> requestMap(DBCollection<?> collection, QueryBuilder query) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("distinct", collection.collectionName());
        map.put("key", field);
        map.put("query", query.build());
        return map;
    }

}