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

public class QueryRequest extends CollectionRequest<QueryResponse> {

	private final QueryOptionsBuilder builder;
	private final int numberToSkip;
	private final int numberToReturn;
	private final BSON query;
	private final boolean closeCursor;

	public QueryRequest(String collectionName, QueryOptionsBuilder builder, int numberToSkip, int numberToReturn, boolean closeCursor, Map<String, Object> query) {
		super(collectionName);
		this.builder = builder;
		this.numberToSkip = numberToSkip;
		this.numberToReturn = Math.abs(numberToReturn);
		this.closeCursor = closeCursor;
		this.query = toBson(query);
	}

	@Override
	public int length() {
		return 4 + collectionNameLength() + 4 + 4 + query.length();
	}

	@Override
	public QueryResponse readResponse(LittleEndianDataReader reader) {
		return new QueryResponse(reader);
	}

	@Override
	public void send(LittleEndianDataWriter writer) {
		writer.writeInt(builder.build());
		writer.writeCString(collectionName());
		writer.writeInt(numberToSkip);
		if (closeCursor) {
			if (numberToReturn > 0) {
				writer.writeInt(-numberToReturn);
			} else {
				writer.writeInt(Integer.MIN_VALUE);
			}
		} else {
			writer.writeInt(numberToReturn);
		}
		writer.write(query.bytes());
	}

	@Override
	public RequestOpCode type() {
		return RequestOpCode.OP_QUERY;
	}
}
