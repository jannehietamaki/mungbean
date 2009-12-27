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

import java.util.Map;

import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;
import mungbean.protocol.bson.BSON;

public class DeleteRequest extends CollectionRequest<Void> {
	private final BSON query;

	public DeleteRequest(String collectionName, Map<String, Object> query) {
		super(collectionName);
		this.query = toBson(query);
	}

	@Override
	public int length() {
		return 8 + collectionNameLength() + query.length();
	}

	@Override
	public Void readResponse(LittleEndianDataReader reader) {
		return null;
	}

	@Override
	public void send(LittleEndianDataWriter writer) {
		writer.writeInt(0); // RESERVED
		writer.writeCString(collectionName());
		writer.writeInt(0); // RESERVED
		writer.write(query.bytes());
	}

	@Override
	public RequestOpCode type() {
		return RequestOpCode.OP_DELETE;
	}
}
