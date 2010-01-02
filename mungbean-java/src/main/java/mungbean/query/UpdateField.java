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

import static mungbean.CollectionUtil.map;

import java.util.Arrays;
import java.util.Map;

import mungbean.protocol.message.UpdateOptionsBuilder;

public class UpdateField extends DslField implements UpdateBuilder {
    private final Update update;

    public UpdateField(Update update, Map<String, Object> result, String key) {
        super(key, result);
        this.update = update;
    }

    public UpdateField increment(Number value) {
        put("$inc", map(key(), value));
        return this;
    }

    public UpdateField set(Object value) {
        put("$set", map(key(), value));
        return this;
    }

    public UpdateField unset() {
        put("$unset", map(key(), 1D));
        return this;
    }

    public UpdateField push(Object value) {
        put("$push", map(key(), value));
        return this;
    }

    public UpdateField push(Object... value) {
        put("$pushAll", map(key(), Arrays.asList(value)));
        return this;
    }

    public UpdateField popLast() {
        put("$pop", map(key(), 1D));
        return this;
    }

    public UpdateField popFirst() {
        put("$pop", map(key(), -1D));
        return this;
    }

    public UpdateField pull(Object item) {
        put("$pull", map(key(), item));
        return this;
    }

    public UpdateField pull(Object... items) {
        put("$pullAll", map(key(), Arrays.asList(items)));
        return this;
    }

    @Override
    public UpdateOptionsBuilder options() {
        return update.options();
    }

}
