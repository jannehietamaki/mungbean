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

package mungbean.protocol.command.admin;

import java.util.LinkedHashMap;
import java.util.Map;

public class IndexOptionsBuilder implements IndexOptions {
    private boolean unique = false;
    private boolean dropDups = false;
    private final Map<String, Double> indices = new LinkedHashMap<String, Double>();

    public IndexOptionsBuilder unique() {
        unique = true;
        return this;
    }

    public IndexOptionsBuilder dropDups() {
        dropDups = true;
        return this;
    }

    public Map<String, Object> build() {
        return new LinkedHashMap<String, Object>() {
            {
                put("unique", unique);
                put("dropDups", dropDups);
            }
        };
    }

    public IndexOptionsField field(String key) {
        return new IndexOptionsField(this, indices, key);
    }

    public Map<String, Double> fields() {
        return indices;
    }

    public String name() {
        StringBuilder ret = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Double> entry : indices.entrySet()) {
            if (!first) {
                ret.append("-");
            }
            ret.append(entry.getKey());
            ret.append("_");
            ret.append(entry.getValue());
            first = false;
        }
        return ret.toString();
    }
}
