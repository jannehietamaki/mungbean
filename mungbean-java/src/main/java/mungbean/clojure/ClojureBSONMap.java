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
import java.util.Iterator;
import java.util.List;

import mungbean.protocol.bson.AbstractBSONMap;
import mungbean.protocol.bson.KeyValuePair;
import clojure.lang.IPersistentMap;
import clojure.lang.Keyword;
import clojure.lang.MapEntry;
import clojure.lang.PersistentHashMap;
import clojure.lang.Symbol;

public class ClojureBSONMap extends AbstractBSONMap<IPersistentMap> {

	public ClojureBSONMap() {
		super(IPersistentMap.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Iterable<KeyValuePair<String, Object>> entriesOf(IPersistentMap item) {
		Iterator<MapEntry> entries = item.iterator();
		List<KeyValuePair<String, Object>> returnValue = new ArrayList<KeyValuePair<String, Object>>();
		while (entries.hasNext()) {
			MapEntry entry = entries.next();
			returnValue.add(new KeyValuePair<String, Object>(toString(entry.getKey()), entry.getValue()));
		}
		return returnValue;
	}

	private String toString(Object key) {
		if (key instanceof Keyword) {
			Keyword keyword = (Keyword) key;
			return keyword.getName();
		}
		return key.toString();
	}

	@Override
	protected IPersistentMap newInstance() {
		return PersistentHashMap.create();
	}

	@Override
	protected IPersistentMap setValue(IPersistentMap item, String key, Object value) {
		return item.assoc(Keyword.intern(Symbol.intern(key)), value);
	}
}
