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

package mungbean.protocol.command;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mungbean.DBCollection;
import mungbean.protocol.bson.Code;
import mungbean.protocol.message.CommandResponse;
import mungbean.query.QueryBuilder;

public class Group extends Aggregation<List<Map<String, Object>>> {
    private final String[] keys;
    private final Map<String, Double> initialValues;
    private final String reduceScript;
    private String finalizeScript = null;
    private final String keyFunction;

    public Group(String[] keys, Map<String, Double> initialValues, String reduceScript) {
        this.keys = keys;
        this.keyFunction = null;
        this.initialValues = initialValues;
        this.reduceScript = reduceScript;
    }

    public Group(String keyFunction, Map<String, Double> initialValues, String reduceScript) {
        this.keys = null;
        this.keyFunction = keyFunction;
        this.initialValues = initialValues;
        this.reduceScript = reduceScript;
    }

    public void setFinalizeScript(String script) {
        finalizeScript = script;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> parseResponse(CommandResponse values) {
        return (List<Map<String, Object>>) values.get("retval");
    }

    @Override
    public Map<String, Object> requestMap(DBCollection<?> collection, QueryBuilder queryBuilder) {
        Map<String, Object> query = queryBuilder.build();
        Map<String, Object> map = new LinkedHashMap<String, Object>();

        Map<String, Object> group = new LinkedHashMap<String, Object>();
        map.put("group", group);
        group.put("initial", initialValues);
        if (query != null && !query.isEmpty()) {
            group.put("cond", query);
        }
        group.put("ns", collection.collectionName());
        if (keys != null) {
            group.put("key", keyMap());
        }
        if (keyFunction != null) {
            group.put("keys", new Code(keyFunction));
        }
        group.put("$reduce", new Code(reduceScript));
        if (finalizeScript != null) {
            group.put("finallize", new Code(finalizeScript));
        }
        return map;

    }

    private Map<String, Object> keyMap() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        for (String key : keys) {
            map.put(key, true);
        }
        return map;
    }

}
