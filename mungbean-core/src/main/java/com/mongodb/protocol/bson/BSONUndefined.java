package com.mongodb.protocol.bson;

import com.mongodb.protocol.LittleEndianDataReader;
import com.mongodb.protocol.LittleEndianDataWriter;

public class BSONUndefined extends BSONCoder<Void> {

	protected BSONUndefined() {
		super(6, Void.class);
	}

	@Override
	protected Void decode(BSONCoders bson, LittleEndianDataReader reader) {
		return null;
	}

	@Override
	protected void encode(BSONCoders bson, Void o, LittleEndianDataWriter writer) {
	}
}
