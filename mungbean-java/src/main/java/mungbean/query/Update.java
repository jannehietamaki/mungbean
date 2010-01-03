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

import mungbean.protocol.message.UpdateOptionsBuilder;

public class Update implements UpdateBuilder {
    private final Map<String, Object> map;
    private final UpdateOptionsBuilder options;

    public Update() {
        map = new LinkedHashMap<String, Object>();
        options = new UpdateOptionsBuilder();
    }

    protected Update(Map<String, Object> updates) {
        this.map = updates;
        this.options = new UpdateOptionsBuilder();
    }

    public UpdateField field(String key) {
        return new UpdateField(this, map, key);
    }

    public Update increment(Map<String, Object> values) {
        put("$inc", values);
        return this;
    }

    public Update set(Map<String, Object> values) {
        put("$set", values);
        return this;
    }

    public Update unset(String... keys) {
        put("$unset", makeMap(keys, 1D));
        return this;
    }

    public Update push(Map<String, Object> values) {
        put("$push", values);
        return this;
    }

    public Update popLast(String... keys) {
        put("$pop", makeMap(keys, 1D));
        return this;
    }

    public Update popFirst(String... keys) {
        put("$pop", makeMap(keys, -1D));
        return this;
    }

    public Map<String, Object> build() {
        return map;
    }

    @Override
    public UpdateOptionsBuilder options() {
        return options;
    }

    private Map<String, Object> makeMap(String[] keys, Object value) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        for (String key : keys) {
            map.put(key, value);
        }
        return map;
    }

    protected void put(String mapKey, Map<String, Object> value) {
        DslField.put(map, mapKey, value);
    }
}
