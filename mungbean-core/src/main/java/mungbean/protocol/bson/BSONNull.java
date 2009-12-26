package com.mongodb.protocol.bson;

import com.mongodb.protocol.LittleEndianDataReader;
import com.mongodb.protocol.LittleEndianDataWriter;

public class BSONNull extends BSONCoder<Void> {

	protected BSONNull() {
		super(10, null);
	}

	@Override
	protected Void decode(BSONCoders bson, LittleEndianDataReader reader) {
		return null;
	}

	@Override
	protected void encode(BSONCoders bson, Void o, LittleEndianDataWriter writer) {
	}

	@Override
	public boolean canEncode(Object value) {
		return value == null;
	}

}
