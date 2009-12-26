package com.mongodb.protocol.bson;

import com.mongodb.protocol.LittleEndianDataReader;
import com.mongodb.protocol.LittleEndianDataWriter;

public class BSONCode extends BSONCoder<Void> {

	protected BSONCode() {
		super(13, Void.class);
	}

	@Override
	protected Void decode(BSONCoders bson, LittleEndianDataReader reader) {
		throw new UnsupportedOperationException("CODE is not supported");
	}

	@Override
	protected void encode(BSONCoders bson, Void o, LittleEndianDataWriter writer) {

	}

}
