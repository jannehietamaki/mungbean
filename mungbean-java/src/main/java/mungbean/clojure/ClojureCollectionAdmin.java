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

package mungbean.clojure;

import mungbean.CollectionAdmin;
import mungbean.protocol.command.admin.IndexOptionsBuilder;
import clojure.lang.IPersistentStack;
import clojure.lang.Keyword;

public class ClojureCollectionAdmin extends CollectionAdmin {

    public ClojureCollectionAdmin(ClojureDBCollection collection) {
        super(collection);
    }

    public void ensureIndex(boolean unique, boolean dropDups, IPersistentStack fields) {
        IndexOptionsBuilder options = new IndexOptionsBuilder();
        while (fields.count() > 0) {
            Object fieldName = fields.peek();
            if (fieldName instanceof Keyword) {
                Keyword k = (Keyword) fieldName;
                fieldName = k.getName();
            }
            options.field(fieldName.toString()).ascending();
            fields = fields.pop();
        }
        if (unique) {
            options.unique();
        }
        if (dropDups) {
            options.dropDups();
        }
        ensureIndex(options);
    }
}
