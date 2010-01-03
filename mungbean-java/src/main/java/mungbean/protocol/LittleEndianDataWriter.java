/*   Copyright 2009 Janne Hietamäki
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
package mungbean.protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import mungbean.Assert;

public class LittleEndianDataWriter {
    private final static Charset UTF8 = Charset.forName("UTF-8");
    private final OutputStream out;

    public LittleEndianDataWriter(OutputStream output) {
        Assert.notNull(output, "OutputStream can not be null");
        this.out = output;
    }

    public void writeByte(byte byteValue) {
        try {
            out.write(byteValue);
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    public void writeInt(int intValue) {
        writeBytes(intValue, 4);
    }

    public void writeShort(int shortValue) {
        writeBytes(shortValue, 2);
    }

    public void write(byte[] bytes) {
        try {
            out.write(bytes);
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    public void writeLong(long value) {
        writeBytes(value, 8);
    }

    private void writeBytes(long value, int bytes) {
        try {
            for (int a = 0; a < bytes; a++) {
                out.write((byte) ((value >> (a * 8)) & 0xFF));
            }
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    public void writeDouble(double value) {
        writeLong(Double.doubleToLongBits(value));
    }

    public void writeCStringWithLength(String value) {
        byte[] cstring = value.getBytes(UTF8);
        writeInt(cstring.length + 1);
        write(cstring);
        writeByte((byte) 0);
    }

    public void writeCString(String value) {
        write(value.getBytes(UTF8));
        writeByte((byte) 0);
    }
}