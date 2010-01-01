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

package mungbean.protocol.command.admin;

import java.util.LinkedHashMap;
import java.util.Map;

import mungbean.DBCollection;
import mungbean.protocol.command.AbstractCommand;
import mungbean.protocol.message.CommandResponse;
import mungbean.protocol.message.NoResponseExpected;

public class EnsureIndex extends AbstractCommand<NoResponseExpected> {

	private final String[] fields;
	private final IndexOptionsBuilder builder;

	public EnsureIndex(String[] fields, IndexOptionsBuilder builder) {
		this.fields = fields;
		this.builder = builder;
	}

	@Override
	public Map<String, Object> requestMap(DBCollection<?> collection) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.putAll(toMap());
		map.putAll(builder.build());
		return map;
	}

	private Map<String, Object> toMap() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		double value = 1D;
		for (String field : fields) {
			map.put(field, value);
			value = -1D;
		}
		return map;
	}

	@Override
	public NoResponseExpected parseResponse(CommandResponse values) {
		return new NoResponseExpected();
	}

}
