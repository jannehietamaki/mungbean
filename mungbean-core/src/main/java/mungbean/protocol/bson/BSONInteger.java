package com.mongodb.protocol.bson;

import com.mongodb.protocol.LittleEndianDataReader;
import com.mongodb.protocol.LittleEndianDataWriter;

public class BSONInteger extends BSONCoder<Integer> {
	protected BSONInteger() {
		super(16, Integer.class);
	}

	@Override
	protected Integer decode(BSONCoders bson, LittleEndianDataReader reader) {
		return reader.readInt();
	}

	@Override
	protected void encode(BSONCoders bson, Integer value, LittleEndianDataWriter writer) {
		writer.writeInt(value);
	}
}
