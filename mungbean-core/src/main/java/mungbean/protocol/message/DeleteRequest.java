package com.mongodb.protocol.message;

import java.util.Map;

import com.mongodb.protocol.LittleEndianDataReader;
import com.mongodb.protocol.LittleEndianDataWriter;
import com.mongodb.protocol.bson.BSON;

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
