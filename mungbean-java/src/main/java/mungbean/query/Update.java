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

package mungbean.query;

import java.util.LinkedHashMap;
import java.util.Map;

import mungbean.protocol.message.UpdateOptionsBuilder;

public class Update implements UpdateBuilder {
	private final Map<String, Object> map;
	private final UpdateOptionsBuilder options;

	public Update() {
		map = new LinkedHashMap<String, Object>();
		options = new UpdateOptionsBuilder();
	}

	protected Update(Map<String, Object> updates) {
		this.map = updates;
		this.options = new UpdateOptionsBuilder();
	}

	public UpdateField field(String key) {
		return new UpdateField(this, map, key);
	}

	public Map<String, Object> build() {
		return map;
	}

	@Override
	public UpdateOptionsBuilder options() {
		return options;
	}
}
