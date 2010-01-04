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

import java.util.Map;

import mungbean.protocol.message.CommandResponse;

public class MapReduceResult {
    private final String collectionName;
    private final int inputCount;
    private final int emitCount;
    private final int outputCount;

    private final int timeMillis;
    private final boolean ok;

    @SuppressWarnings("unchecked")
    public MapReduceResult(CommandResponse values) {
        collectionName = values.getString("result");
        Map<String, Object> counts = (Map<String, Object>) values.get("counts");
        inputCount = getInt(counts, "input");
        emitCount = getInt(counts, "emit");
        outputCount = getInt(counts, "output");
        timeMillis = getInt(values.values(), "timeMillis");
        ok = getInt(values.values(), "ok") == 1;
    }

    public String collectionName() {
        return collectionName;
    }

    public int inputCount() {
        return inputCount;
    }

    public int emitCount() {
        return emitCount;
    }

    public int outputCount() {
        return outputCount;
    }

    public int timeMillis() {
        return timeMillis;
    }

    public boolean wasOk() {
        return ok;
    }

    private int getInt(Map<String, Object> counts, String key) {
        return ((Number) counts.get(key)).intValue();
    }

}
