package mungbean.protocol.bson;

import mungbean.protocol.MongoTimestamp;
import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;

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
