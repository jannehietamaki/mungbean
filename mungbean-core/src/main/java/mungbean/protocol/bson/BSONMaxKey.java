package com.mongodb.protocol.bson;

import com.mongodb.protocol.LittleEndianDataReader;
import com.mongodb.protocol.LittleEndianDataWriter;

public class BSONMaxKey extends BSONCoder<Void> {

	protected BSONMaxKey() {
		super(127, Void.class);
	}

	@Override
	protected Void decode(BSONCoders bson, LittleEndianDataReader reader) {
		return null;
	}

	@Override
	protected void encode(BSONCoders bson, Void o, LittleEndianDataWriter writer) {

	}

}
