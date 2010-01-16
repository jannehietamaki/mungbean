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

import java.util.ArrayList;
import java.util.List;

import clojure.lang.IPersistentMap;
import clojure.lang.Keyword;
import clojure.lang.Symbol;
import mungbean.Authentication;
import mungbean.DBOperationExecutor;
import mungbean.Settings;
import mungbean.SingleNodeDbOperationExecutor;
import mungbean.Server;

public class ClojureMungbean {
    private final DBOperationExecutor executor;

    public ClojureMungbean(String host, int port, IPersistentMap... authentications) {
        List<Authentication> auths = new ArrayList<Authentication>();
        for (IPersistentMap authentication : authentications) {
            String database = get(authentication, "database");
            String user = get(authentication, "user");
            String password = get(authentication, "password");
            if (database != null) {
                auths.add(new Authentication(database, user, password));
            }
        }
        // TODO make settings configurable from clojure
        executor = new SingleNodeDbOperationExecutor(new Settings(), new Server(host, port, auths.toArray(new Authentication[auths.size()])));
    }

    public ClojureDatabase openDatabase(String name) {
        return new ClojureDatabase(executor, name);
    }

    public void close() {
        executor.close();
    }

    private String get(IPersistentMap map, String key) {
        Object value = map.valAt(Keyword.intern(Symbol.create(key)));
        if (value == null) {
            return null;
        }
        return String.valueOf(value);
    }

}
