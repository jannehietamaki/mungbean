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
