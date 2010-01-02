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

public abstract class AbstractBSONArray<Type, ItemType> extends BSONCoder<Type> {

    public AbstractBSONArray(Class<Type> javaType) {
        super(4, javaType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Type decode(AbstractBSONCoders bson, LittleEndianDataReader reader) {
        reader.readInt(); // skip size
        Type ret = newInstance();
        BSONCoder<?> b;
        while (!(b = bson.forType((byte) reader.readByte())).isEndMarker()) {
            reader.readCString(); // Skip name = index
            ItemType value = (ItemType) b.read(bson, reader);
            ret = addValue(ret, value);
        }
        return ret;
    }

    protected abstract Type addValue(Type ret, ItemType value);

    protected abstract Iterable<KeyValuePair<Integer, ItemType>> valuesOf(Type l);

    protected abstract Type newInstance();

    @Override
    protected void encode(AbstractBSONCoders bson, Type l, LittleEndianDataWriter writer) {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        LittleEndianDataWriter out = new LittleEndianDataWriter(byteOut);
        Iterable<KeyValuePair<Integer, ItemType>> values = valuesOf(l);
        for (KeyValuePair<Integer, ItemType> pair : values) {
            ItemType value = pair.value();
            bson.forValue(value).write(bson, String.valueOf(pair.key()), value, out);
        }
        byte[] bytes = byteOut.toByteArray();
        writer.writeInt(bytes.length + 4);
        writer.write(bytes);
        writer.writeByte(BSONEndMarker.instance().type());
    }
}