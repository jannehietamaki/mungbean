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

package mungbean.clojure;

import java.util.concurrent.atomic.AtomicReference;

import mungbean.AbstractDBCollection;
import mungbean.DBOperationExecutor;
import mungbean.ObjectId;
import mungbean.QueryCallback;
import mungbean.protocol.bson.BSONCoder;
import mungbean.query.Query;
import clojure.lang.IFn;
import clojure.lang.IPersistentCollection;
import clojure.lang.IPersistentMap;
import clojure.lang.Keyword;
import clojure.lang.PersistentList;
import clojure.lang.Symbol;

public class ClojureDBCollection extends AbstractDBCollection<IPersistentMap> {

    public ClojureDBCollection(DBOperationExecutor executor, String dbName, String collectionName) {
        super(executor, dbName, collectionName, new ClojureBSONCoders(), new ClojureBSONCoders());
    }

    @Override
    public BSONCoder<IPersistentMap> defaultEncoder() {
        return new ClojureBSONMap();
    }

    @Override
    protected IPersistentMap injectId(IPersistentMap doc) {
        Keyword keyword = Keyword.intern(Symbol.intern("_id"));
        if (!doc.containsKey(keyword)) {
            return doc.assoc(keyword, new ObjectId());
        }
        return doc;
    }

    public IPersistentCollection query(Query query, final IFn callback) {
        final AtomicReference<IPersistentCollection> result = new AtomicReference<IPersistentCollection>(PersistentList.EMPTY);
        query(query, new QueryCallback<IPersistentMap>() {
            @Override
            public boolean process(IPersistentMap item) {
                try {
                    result.set(result.get().cons(callback.invoke(item)));
                    return true;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return result.get();
    }

}
