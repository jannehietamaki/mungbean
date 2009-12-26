package com.mongodb.protocol.message;

import com.mongodb.protocol.LittleEndianDataReader;
import com.mongodb.protocol.LittleEndianDataWriter;

public class GetMoreRequest extends CollectionRequest<QueryResponse> {
	private final long cursorId;
	private final int numberToReturn;

	public GetMoreRequest(String collectionName, long cursorId, int numberToReturn) {
		super(collectionName);
		this.cursorId = cursorId;
		this.numberToReturn = numberToReturn;
	}

	@Override
	public int length() {
		return 4 + collectionNameLength() + 4 + 8;
	}

	@Override
	public QueryResponse readResponse(LittleEndianDataReader reader) {
		return new QueryResponse(reader);
	}

	@Override
	public void send(LittleEndianDataWriter writer) {
		writer.writeInt(0); // RESERVED
		writer.writeCString(collectionName());
		writer.writeInt(numberToReturn);
		writer.writeLong(cursorId);
	}

	@Override
	public RequestOpCode type() {
		return RequestOpCode.OP_GET_MORE;
	}
}
