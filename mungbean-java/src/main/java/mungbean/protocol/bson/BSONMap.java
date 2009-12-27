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
package mungbean.protocol.bson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BSONMap extends AbstractBSONMap<Map<String, Object>> {
	private final static Class<?> typeClass = Map.class;

	@SuppressWarnings("unchecked")
	public BSONMap() {
		super((Class<Map<String, Object>>) typeClass);
	}

	@Override
	protected Map<String, Object> newInstance() {
		return new HashMap<String, Object>();
	}

	@Override
	protected Map<String, Object> setValue(Map<String, Object> item, String key, Object value) {
		item.put(key, value);
		return item;
	}

	@Override
	protected Iterable<KeyValuePair<String, Object>> entriesOf(Map<String, Object> item) {
		List<KeyValuePair<String, Object>> returnValue = new ArrayList<KeyValuePair<String, Object>>();
		for (Map.Entry<String, Object> entry : item.entrySet()) {
			returnValue.add(new KeyValuePair<String, Object>(entry.getKey(), entry.getValue()));
		}
		return returnValue;
	}
}
