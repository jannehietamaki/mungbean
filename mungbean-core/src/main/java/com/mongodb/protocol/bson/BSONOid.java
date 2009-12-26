package com.mongodb.protocol.bson;

import com.mongodb.ObjectId;
import com.mongodb.protocol.LittleEndianDataReader;
import com.mongodb.protocol.LittleEndianDataWriter;

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
