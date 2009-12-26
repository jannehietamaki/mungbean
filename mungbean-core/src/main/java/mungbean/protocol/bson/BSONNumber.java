package com.mongodb.protocol.bson;

import com.mongodb.protocol.LittleEndianDataReader;
import com.mongodb.protocol.LittleEndianDataWriter;

public class BSONNumber extends BSONCoder<Double> {
	protected BSONNumber() {
		super(1, Double.class);
	}

	@Override
	protected Double decode(BSONCoders bson, LittleEndianDataReader reader) {
		return Double.longBitsToDouble(reader.readLong());
	}

	@Override
	protected void encode(BSONCoders bson, Double value, LittleEndianDataWriter writer) {
		writer.writeDouble(value);
	}

}
