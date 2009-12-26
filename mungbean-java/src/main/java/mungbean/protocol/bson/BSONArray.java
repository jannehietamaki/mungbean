package mungbean.protocol.bson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;

public class BSONArray extends BSONCoder<List<?>> {

	protected BSONArray() {
		super(4, List.class);
	}

	@Override
	public List<?> decode(BSONCoders bson, LittleEndianDataReader reader) {
		reader.readInt(); // skip size
		List<Object> ret = new ArrayList<Object>();
		BSONCoder<?> b;
		while (!(b = bson.forType((byte) reader.readByte())).isEndMarker()) {
			reader.readCString(); // Skip name = index
			Object value = b.read(bson, reader);
			ret.add(value);
		}
		return ret;
	}

	@Override
	protected void encode(BSONCoders bson, List<?> l, LittleEndianDataWriter writer) {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		LittleEndianDataWriter out = new LittleEndianDataWriter(byteOut);
		for (int i = 0; i < l.size(); i++) {
			Object value = l.get(i);
			bson.forValue(value).write(bson, String.valueOf(i), value, out);
		}
		byte[] bytes = byteOut.toByteArray();
		writer.writeInt(bytes.length + 4);
		writer.write(bytes);
		writer.writeByte(BSONEndMarker.instance().type());
	}
}
