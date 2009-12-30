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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import scala.actors.threadpool.Arrays;

public class CollectionUtil {

	public static Map<String, Object> map(final String key, final Object value) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put(key, value);
		return map;
	}

	public static Map<String, Object> merge(Map<String, Object>... maps) {
		Map<String, Object> ret = new LinkedHashMap<String, Object>();
		for (Map<String, Object> map : maps) {
			ret.putAll(map);
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public static List<Object> list(Object... items) {
		return Arrays.asList(items);
	}

	public static List<Object> merge(List<Object>... lists) {
		List<Object> ret = new ArrayList<Object>();
		for (List<Object> list : lists) {
			ret.addAll(list);
		}
		return ret;
	}
}
