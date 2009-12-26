package mungbean.protocol.bson;

import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;

public class BSONBoolean extends BSONCoder<Boolean> {

	protected BSONBoolean() {
		super(8, Boolean.class);
	}

	@Override
	protected Boolean decode(BSONCoders bson, LittleEndianDataReader reader) {
		return reader.readByte() > 0;
	}

	@Override
	protected void encode(BSONCoders bson, Boolean value, LittleEndianDataWriter writer) {
		writer.writeByte((byte) (value ? 1 : 0));
	}
}
