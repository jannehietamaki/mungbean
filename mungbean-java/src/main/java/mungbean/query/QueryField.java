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

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;

import mungbean.protocol.bson.Code;

import static mungbean.CollectionUtil.map;

public class QueryField extends DslField implements QueryBuilder {
    private final Query query;
    private final Map<String, Object> orderMap;

    QueryField(Query query, Map<String, Object> result, Map<String, Object> orderMap, String key) {
        super(key, result);
        this.query = query;
        this.orderMap = orderMap;
    }

    public QueryField is(Object value) {
        putResult(value);
        return this;
    }

    public QueryField matches(Pattern pattern) {
        return is(pattern);
    }

    public QueryField lessThan(Object value) {
        put(map("$lt", value));
        return this;
    }

    public QueryField greaterThan(Object value) {
        put(map("$gt", value));
        return this;
    }

    public QueryField lessOrEqual(Object value) {
        put(map("$lte", value));
        return this;
    }

    public QueryField greaterOrEqual(Object value) {
        put(map("$gte", value));
        return this;
    }

    public QueryField not(Object value) {
        put(map("$ne", value));
        return this;
    }

    public QueryField in(Object... values) {
        put(map("$in", Arrays.asList(values)));
        return this;
    }

    public QueryField notIn(Object... values) {
        put(map("$nin", Arrays.asList(values)));
        return this;
    }

    public QueryField all(Object... values) {
        put(map("$all", Arrays.asList(values)));
        return this;
    }

    public QueryField size(int value) {
        put(map("$size", value));
        return this;
    }

    public QueryField exists(boolean value) {
        put(map("$exists", value));
        return this;
    }

    public QueryField where(String script) {
        putResult(new Code(script));
        return this;
    }

    public QueryField orderAscending() {
        orderMap.put(key(), 1D);
        return this;
    }

    public QueryField orderDescending() {
        orderMap.put(key(), -1D);
        return this;
    }

    @Override
    public int limit() {
        return query.limit();
    }

    @Override
    public int skip() {
        return query.skip();
    }

    @Override
    public Map<String, Object> order() {
        return orderMap;
    }
}
