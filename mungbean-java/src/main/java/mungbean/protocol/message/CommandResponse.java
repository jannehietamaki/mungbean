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

package mungbean.protocol.message;

import java.util.Map;

public class CommandResponse implements Response {
    private final Map<String, Object> map;

    public CommandResponse(Map<String, Object> map) {
        this.map = map;
    }

    public Long getLong(String key) {
        return ((Number) get(key)).longValue();
    }

    public Object get(String key) {
        return map.get(key);
    }

    public String getString(String key) {
        Object value = get(key);
        if (value != null) {
            return String.valueOf(value);
        }
        return null;
    }

    public Map<String, Object> values() {
        return map;
    }

    @Override
    public int responseTo() {
        return -1;
    }

    @Override
    public long cursorId() {
        return 0;
    }

}
