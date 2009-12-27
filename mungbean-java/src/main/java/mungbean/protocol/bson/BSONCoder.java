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

import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;

public abstract class BSONCoder<JavaType> {

	private final byte bsonType;
	private final Class<?> javaType;

	protected BSONCoder(int typeId, Class<?> javaType) {
		this.bsonType = (byte) typeId;
		this.javaType = javaType;
	}

	public byte type() {
		return bsonType;
	}

	public boolean canEncode(Object val) {
		return val != null && javaType.isAssignableFrom(val.getClass());
	}

	public static boolean isdbOnlyField(String s) {
		return s.equals("_ns") || s.equals("_save") || s.equals("_update");
	}

	protected abstract JavaType decode(BSONCoders bson, LittleEndianDataReader reader);

	protected abstract void encode(BSONCoders bson, JavaType o, LittleEndianDataWriter writer);

	public JavaType read(BSONCoders bson, LittleEndianDataReader reader) {
		return decode(bson, reader);
	}

	public void write(BSONCoders bson, String name, JavaType o, LittleEndianDataWriter writer) {
		writer.writeByte(type());
		writer.writeCString(name);
		encode(bson, o, writer);
	}

	public String readPath(LittleEndianDataReader reader, String path) {
		String name = reader.readCString();
		if (path.isEmpty()) {
			return name;
		}
		return path + "." + name;
	}

	public boolean isEndMarker() {
		return false;
	}

	public BSON write(BSONCoders bson, JavaType o) {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		encode(bson, o, new LittleEndianDataWriter(byteOut));
		return new BSON(byteOut.toByteArray());
	}

}
