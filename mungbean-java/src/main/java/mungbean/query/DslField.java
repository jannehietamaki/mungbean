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

import java.util.Map;

public class DslField {
    private final Map<String, Object> result;
    private final String key;

    public DslField(String key, Map<String, Object> result) {
        this.key = key;
        this.result = result;
    }

    protected void putResult(Object value) {
        result.put(key, value);
    }

    protected String key() {
        return key;
    }

    protected void put(Map<String, Object> value) {
        put(result, key, value);
    }

    public void put(String mapKey, Map<String, Object> value) {
        put(result, mapKey, value);
    }

    @SuppressWarnings("unchecked")
    public static void put(Map<String, Object> target, String mapKey, Map<String, Object> value) {
        Object val = target.get(mapKey);
        if (val != null) {
            if (!(val instanceof Map)) {
                throw new IllegalArgumentException("Invalid combination of arguments");
            }
            Map<String, Object> valueMap = (Map<String, Object>) val;
            valueMap.putAll(value);
        } else {
            target.put(mapKey, value);
        }
    }

    public Map<String, Object> build() {
        return result;
    }
}
