package mungbean.protocol.bson;

import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;

public class BSONEndMarker extends BSONCoder<Void> {
	private final static BSONEndMarker instance = new BSONEndMarker();

	public BSONEndMarker() {
		super(0, null);
	}

	@Override
	protected Void decode(BSONCoders bson, LittleEndianDataReader reader) {
		return null;
	}

	@Override
	protected void encode(BSONCoders bson, Void o, LittleEndianDataWriter writer) {

	}

	@Override
	public boolean canEncode(Object val) {
		return false;
	}

	public static BSONEndMarker instance() {
		return instance;
	}

	@Override
	public boolean isEndMarker() {
		return true;
	}
}
