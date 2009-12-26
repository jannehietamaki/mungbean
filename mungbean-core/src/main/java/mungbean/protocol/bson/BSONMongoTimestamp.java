package com.mongodb.protocol.bson;

import com.mongodb.protocol.MongoTimestamp;
import com.mongodb.protocol.LittleEndianDataReader;
import com.mongodb.protocol.LittleEndianDataWriter;

public class BSONMongoTimestamp extends BSONCoder<MongoTimestamp> {
	protected BSONMongoTimestamp() {
		super(18, MongoTimestamp.class);
	}

	@Override
	protected MongoTimestamp decode(BSONCoders bson, LittleEndianDataReader reader) {
		return new MongoTimestamp(reader.readInt(), reader.readInt());
	}

	@Override
	protected void encode(BSONCoders bson, MongoTimestamp ts, LittleEndianDataWriter writer) {
		writer.writeInt(ts.time());
		writer.writeInt(ts.incrementedField());
	}
}
