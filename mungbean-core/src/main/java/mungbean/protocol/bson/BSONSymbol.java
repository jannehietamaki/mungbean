package com.mongodb.protocol.bson;

import com.mongodb.protocol.LittleEndianDataReader;
import com.mongodb.protocol.LittleEndianDataWriter;

public class BSONSymbol extends BSONCoder<String> {

	protected BSONSymbol() {
		super(14, String.class);
	}

	@Override
	protected String decode(BSONCoders bson, LittleEndianDataReader reader) {
		reader.readInt(); // Skip length
		return reader.readCString();
	}

	@Override
	protected void encode(BSONCoders bson, String value, LittleEndianDataWriter writer) {
		writer.writeCStringWithLength(value);
	}

}
