package com.mongodb.protocol.message;

import com.mongodb.protocol.LittleEndianDataReader;

public class MongoResponse {

	private final int messageLength;
	private final int requestId;
	private final int responseTo;
	private final RequestOpCode opCode;

	public MongoResponse(LittleEndianDataReader reader) {
		messageLength = reader.readInt();
		requestId = reader.readInt();
		responseTo = reader.readInt();
		opCode = RequestOpCode.forId(reader.readInt());
	}

	public int messageLength() {
		return messageLength;
	}

	public int requestId() {
		return requestId;
	}

	public int responseTo() {
		return responseTo;
	}

	public RequestOpCode opCode() {
		return opCode;
	}

}
