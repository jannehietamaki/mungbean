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

import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;

public class BSONBinary extends BSONCoder<byte[]> {
    static final byte FUNCTION = 1;
    static final byte BINARY = 2;
    static final byte UUID = 3;
    static final byte MD5 = 5;
    static final byte USER_DEFINED = (byte) 0x80;

    protected BSONBinary() {
        super(5, byte[].class);
    }

    @Override
    protected byte[] decode(AbstractBSONCoders bson, LittleEndianDataReader reader) {
        int totalLength = reader.readInt();
        int binaryType = reader.readByte();

        if (binaryType == BINARY) {
            int length = reader.readInt();
            if (length + 4 != totalLength) {
                throw new RuntimeException("Bad data size! " + length + " != " + totalLength);
            }
            byte[] data = new byte[length];
            reader.read(data);
            return data;
        }

        byte[] data = new byte[totalLength];
        reader.read(data);
        return data;
    }

    @Override
    protected void encode(AbstractBSONCoders bson, byte[] data, LittleEndianDataWriter writer) {
        writer.writeInt(4 + data.length);
        writer.writeByte(BINARY);
        writer.writeInt(data.length);
        writer.write(data);
    }
}
