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

package mungbean.protocol.command.admin;

import java.util.Map;

public class IndexOptionsField implements IndexOptions {
    private final IndexOptionsBuilder builder;
    private final String key;
    private final Map<String, Double> indices;

    public IndexOptionsField(IndexOptionsBuilder builder, Map<String, Double> indices, String key) {
        this.builder = builder;
        this.key = key;
        this.indices = indices;
        ascending();
    }

    @Override
    public Map<String, Object> build() {
        return builder.build();
    }

    @Override
    public Map<String, Double> fields() {
        return builder.fields();
    }

    @Override
    public String name() {
        return builder.name();
    }

    @Override
    public IndexOptions field(String key) {
        return builder.field(key);
    }

    public void ascending() {
        indices.put(key, 1D);
    }

    public void descending() {
        indices.put(key, -1D);
    }
}
