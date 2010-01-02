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
    private final Map<String, Object> query;
    private final Map<String, Object> orderMap;
    private int skip;
    private int limit;

    public Query() {
        query = new LinkedHashMap<String, Object>();
        orderMap = new LinkedHashMap<String, Object>();
    }

    protected Query(Map<String, Object> query, int skip, int limit) {
        this.query = query;
        orderMap = new LinkedHashMap<String, Object>();
        this.skip = skip;
        this.limit = limit;
    }

    public QueryField field(String key) {
        return new QueryField(this, query, orderMap, key);
    }

    public Query setSkip(int skip) {
        this.skip = skip;
        return this;
    }

    public Query setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public Map<String, Object> build() {
        return query;
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
