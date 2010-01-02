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

public abstract class AbstractBSONMap<T> extends BSONCoder<T> {

    public AbstractBSONMap(Class<T> javaType) {
        super(3, javaType);
    }

    @Override
    protected T decode(AbstractBSONCoders bson, LittleEndianDataReader reader) {
        reader.readInt(); // Skip length
        T ret = newInstance();
        BSONCoder<?> b;
        while (!(b = bson.forType((byte) reader.readByte())).isEndMarker()) {
            String name = b.readPath(reader, "");
            Object value = b.read(bson, reader);
            ret = setValue(ret, name, value);
        }
        return ret;
    }

    protected abstract T newInstance();

    protected abstract T setValue(T item, String key, Object value);

    protected abstract Iterable<KeyValuePair<String, Object>> entriesOf(T item);

    @Override
    public void encode(AbstractBSONCoders bson, T o, LittleEndianDataWriter writer) {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        LittleEndianDataWriter localWriter = new LittleEndianDataWriter(byteOut);
        for (KeyValuePair<String, Object> entry : entriesOf(o)) {
            bson.forValue(entry.value()).write(bson, entry.key(), entry.value(), localWriter);
        }
        byte[] bytes = byteOut.toByteArray();
        writer.writeInt(bytes.length + 4 + 1);
        writer.write(bytes);
        writer.writeByte(BSONEndMarker.instance().type());
    }
}