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

import java.util.Iterator;
import java.util.Map;

import mungbean.query.Query;
import clojure.lang.IPersistentMap;
import clojure.lang.Keyword;
import clojure.lang.MapEntry;
import clojure.lang.Symbol;

public class ClojureQuery extends Query {
    private static final Keyword DESC = Keyword.intern(Symbol.intern("desc"));

    @SuppressWarnings("unchecked")
    public ClojureQuery(Map<String, Object> query, int skip, int limit, IPersistentMap order) {
        super(query, skip, limit);
        Iterator<MapEntry> i = order.iterator();
        while (i.hasNext()) {
            MapEntry entry = i.next();
            if (entry.val().equals(DESC)) {
                field(toString(entry.key())).orderDescending();
            } else {
                field(toString(entry.key())).orderAscending();
            }
        }
    }

    private String toString(Object key) {
        if (key instanceof Keyword) {
            Keyword kw = (Keyword) key;
            return kw.getName();
        }
        return key.toString();
    }
}