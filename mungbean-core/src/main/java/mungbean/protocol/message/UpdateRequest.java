package com.mongodb.protocol.message;

import java.util.Map;

import com.mongodb.protocol.LittleEndianDataReader;
import com.mongodb.protocol.LittleEndianDataWriter;
import com.mongodb.protocol.bson.BSON;

public class UpdateRequest extends CollectionRequest<Void> {
	private final UpdateOptionsBuilder flags;
	private final BSON selector;
	private final BSON document;

	public UpdateRequest(String collectionName, UpdateOptionsBuilder flags, Map<String, Object> selector, Map<String, Object> document) {
		super(collectionName);
		this.flags = flags;
		this.selector = toBson(selector);
		this.document = toBson(document);
	}

	@Override
	public int length() {
		return 4 + collectionNameLength() + 4 + selector.length() + document.length();
	}

	@Override
	public Void readResponse(LittleEndianDataReader reader) {
		return null;
	}

	@Override
	public void send(LittleEndianDataWriter writer) {
		writer.writeInt(0); // RESERVED
		writer.writeCString(collectionName());
		writer.writeInt(flags.value());
		writer.write(selector.bytes());
		writer.write(document.bytes());
	}

	@Override
	public RequestOpCode type() {
		return RequestOpCode.OP_UPDATE;
	}

}
