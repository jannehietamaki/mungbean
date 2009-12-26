package mungbean.protocol.message;

import java.nio.charset.Charset;
import java.util.Map;

import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;
import mungbean.protocol.bson.BSON;
import mungbean.protocol.bson.BSONCoders;

public abstract class MongoRequest<ResponseType> {
	protected final static Charset UTF8 = Charset.forName("UTF-8");

	public abstract void send(LittleEndianDataWriter writer);

	public abstract ResponseType readResponse(LittleEndianDataReader reader);

	public abstract RequestOpCode type();

	public abstract int length();

	protected BSON toBson(Map<String, Object> document) {
		return BSONCoders.instance().forValue(document).write(BSONCoders.instance(), document);
	}
}
