package com.mongodb.protocol.message;

import com.mongodb.protocol.LittleEndianDataReader;
import com.mongodb.protocol.LittleEndianDataWriter;

public class KillCursorsRequest extends MongoRequest<Void> {
	private final long[] cursorIds;

	public KillCursorsRequest(long... cursorIds) {
		this.cursorIds = cursorIds;
	}

	@Override
	public int length() {
		return 4 + 4 + cursorIds.length * 8;
	}

	@Override
	public Void readResponse(LittleEndianDataReader reader) {
		return null;
	}

	@Override
	public void send(LittleEndianDataWriter writer) {
		writer.writeInt(0); // RESERVED
		writer.writeInt(cursorIds.length);
		for (long cursorId : cursorIds) {
			writer.writeLong(cursorId);
		}
	}

	@Override
	public RequestOpCode type() {
		return RequestOpCode.OP_KILL_CURSORS;
	}
}
