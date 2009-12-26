package mungbean.protocol.bson;

import mungbean.ObjectId;
import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;

public class BSONOid extends BSONCoder<ObjectId> {

	protected BSONOid() {
		super(7, ObjectId.class);
	}

	@Override
	protected ObjectId decode(BSONCoders bson, LittleEndianDataReader reader) {
		byte[] bytes = new byte[12];
		reader.read(bytes);
		return new ObjectId(bytes);
	}

	@Override
	protected void encode(BSONCoders bson, ObjectId oid, LittleEndianDataWriter writer) {
		writer.write(oid.toByteArray());
	}
}
