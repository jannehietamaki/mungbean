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

package mungbean;

import java.util.LinkedHashMap;
import java.util.Map;

import mungbean.protocol.DBConnection;
import mungbean.protocol.bson.MapBSONCoders;
import mungbean.protocol.command.admin.IndexOptions;
import mungbean.protocol.message.InsertRequest;

public class CollectionAdmin {
    private final AbstractDBCollection<?> collection;

    public CollectionAdmin(AbstractDBCollection<?> collection) {
        this.collection = collection;
    }

    public void ensureIndex(IndexOptions options) {
        final Map<String, Object> doc = new LinkedHashMap<String, Object>();
        doc.put("ns", collection.dbName());
        doc.put("key", options.fields());
        doc.put("name", options.name());
        doc.putAll(options.build());
        collection.execute(new DBConversation<Void>() {
            @SuppressWarnings("unchecked")
            @Override
            public Void execute(DBConnection connection) {
                connection.execute(new InsertRequest<Map<String, Object>>(collection.dbName() + ".system.indexes", new MapBSONCoders(), doc));
                return null;
            }
        });
    }
}
