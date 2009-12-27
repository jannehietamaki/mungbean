/*
   Copyright 2009 Janne Hietamäki

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
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
