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
package mungbean.protocol.message;

import static mungbean.CollectionUtil.map;

import java.util.List;
import java.util.Map;

import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;
import mungbean.protocol.bson.MapBSONCoders;
import mungbean.protocol.bson.BSONMap;

public class CommandRequest extends MongoRequest<Map<String, Object>> {

	private static final BSONMap RESPONSE_CODER = new BSONMap();
	private static final MapBSONCoders CODERS = new MapBSONCoders();
	private final QueryRequest<Map<String, Object>> query;

	public CommandRequest(String dbName, Map<String, Object> content) {
		query = new QueryRequest<Map<String, Object>>(dbName + ".$cmd", new QueryOptionsBuilder(), 0, 1, true, content, null, CODERS, RESPONSE_CODER);
	}

	public CommandRequest(String dbName, String command) {
		this(dbName, map(command, 1D));
	}

	public CommandRequest(String command) {
		query = new QueryRequest<Map<String, Object>>("$cmd", new QueryOptionsBuilder(), 0, 1, true, map(command, 1D), null, CODERS, RESPONSE_CODER);

	}

	@Override
	public int length() {
		return query.length();
	}

	@Override
	public Map<String, Object> readResponse(LittleEndianDataReader reader) {
		List<Map<String, Object>> values = query.readResponse(reader).values();
		if (values.isEmpty()) {
			return null;
		}
		return values.get(0);
	}

	@Override
	public void send(LittleEndianDataWriter writer) {
		query.send(writer);
	}

	@Override
	public RequestOpCode type() {
		return query.type();
	}
}
