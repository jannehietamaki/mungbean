package com.mongodb.protocol.bson;

import com.mongodb.protocol.LittleEndianDataReader;
import com.mongodb.protocol.LittleEndianDataWriter;

public class BSONCodeWithScope extends BSONCoder<Void> {

	protected BSONCodeWithScope() {
		super(15, Void.class);
	}

	@Override
	protected Void decode(BSONCoders bson, LittleEndianDataReader reader) {
		throw new UnsupportedOperationException("CODE_W_SCOPE is not supported");
	}

	@Override
	protected void encode(BSONCoders bson, Void o, LittleEndianDataWriter writer) {

	}

}
