package mungbean.protocol.bson;

import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;

public class BSONNull extends BSONCoder<Void> {

	protected BSONNull() {
		super(10, null);
	}

	@Override
	protected Void decode(BSONCoders bson, LittleEndianDataReader reader) {
		return null;
	}

	@Override
	protected void encode(BSONCoders bson, Void o, LittleEndianDataWriter writer) {
	}

	@Override
	public boolean canEncode(Object value) {
		return value == null;
	}

}
