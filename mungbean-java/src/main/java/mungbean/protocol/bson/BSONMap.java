package mungbean.protocol.bson;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;

public class BSONMap extends BSONCoder<Map<String, Object>> {

	public BSONMap() {
		super(3, Map.class);
	}

	@Override
	protected Map<String, Object> decode(BSONCoders bson, LittleEndianDataReader reader) {
		reader.readInt(); // Skip length
		Map<String, Object> ret = new HashMap<String, Object>();
		BSONCoder<?> b;
		while (!(b = bson.forType((byte) reader.readByte())).isEndMarker()) {
			String name = b.readPath(reader, "");
			Object value = b.read(bson, reader);
			ret.put(name, value);
		}
		return ret;
	}

	@Override
	protected void encode(BSONCoders bson, Map<String, Object> o, LittleEndianDataWriter writer) {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		LittleEndianDataWriter localWriter = new LittleEndianDataWriter(byteOut);
		for (Map.Entry<String, Object> entry : o.entrySet()) {
			bson.forValue(entry.getValue()).write(bson, entry.getKey(), entry.getValue(), localWriter);
		}
		byte[] bytes = byteOut.toByteArray();
		writer.writeInt(bytes.length + 4 + 1);
		writer.write(bytes);
		writer.writeByte(BSONEndMarker.instance().type());
	}
}
