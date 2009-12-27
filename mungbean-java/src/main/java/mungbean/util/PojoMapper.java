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
package mungbean.util;

import java.util.HashMap;
import java.util.Map;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

public class PojoMapper<T> {
	private final static Objenesis objenesis = new ObjenesisStd();
	private final Class<T> typeClass;

	public PojoMapper(Class<T> typeClass) {
		this.typeClass = typeClass;
	}

	@SuppressWarnings("unchecked")
	public T fromMap(Map<String, Object> doc) {
		T ret = (T) objenesis.newInstance(typeClass);
		FieldDefinition[] fields = ReflectionUtil.fieldsOf(typeClass);
		for (FieldDefinition field : fields) {
			field.set(ret, doc.get(field.name()));
		}
		return ret;
	}

	public Map<String, Object> toMap(T doc) {
		Map<String, Object> ret = new HashMap<String, Object>();
		FieldDefinition[] fields = ReflectionUtil.fieldsOf(typeClass);
		for (FieldDefinition field : fields) {
			ret.put(field.name(), field.get(doc));
		}
		return ret;
	}
}
