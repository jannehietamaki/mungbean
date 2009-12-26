package com.mongodb.protocol.bson;

import java.util.Date;

import com.mongodb.protocol.LittleEndianDataReader;
import com.mongodb.protocol.LittleEndianDataWriter;

public class BSONDate extends BSONCoder<Date> {

	protected BSONDate() {
		super(9, Date.class);
	}

	@Override
	protected Date decode(BSONCoders bson, LittleEndianDataReader reader) {
		return new Date(reader.readLong());
	}

	@Override
	protected void encode(BSONCoders bson, Date date, LittleEndianDataWriter writer) {
		writer.writeLong(date.getTime());
	}
}
