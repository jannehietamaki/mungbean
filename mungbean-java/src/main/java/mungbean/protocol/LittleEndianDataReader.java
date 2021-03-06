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
package mungbean.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import mungbean.Assert;

public class LittleEndianDataReader {
    private final static Charset UTF8 = Charset.forName("UTF-8");
    private final InputStream in;

    public LittleEndianDataReader(InputStream input) {
        Assert.notNull(input, "InputStream can not be null");
        this.in = input;
    }

    public int readInt() {
        byte[] buf = new byte[4];
        readBuffer(buf);
        int value = ((buf[3] & 0xFF) << 24) | ((buf[2] & 0xFF) << 16) | ((buf[1] & 0xFF) << 8) | (buf[0] & 0xFF);
        return value;
    }

    private void readBuffer(byte[] buf) {
        try {
            int items = in.read(buf);
            if (items == -1) {
                throw new RuntimeIOException("Remote end has closed the connection");
            }
            if (items != buf.length) {
                throw new RuntimeIOException("Enough data was not available");
            }
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    public short readShort() {
        byte[] buf = new byte[2];
        readBuffer(buf);
        return (short) (((buf[1] & 0xFF) << 8) | (buf[0] & 0xFF));
    }

    public int readByte() {
        try {
            return in.read();
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    public String readCString() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int b;
        while ((b = readByte()) > 0) {
            out.write((byte) b);
        }
        return new String(out.toByteArray(), UTF8);
    }

    public void read(byte[] data) {
        readBuffer(data);
    }

    public long readLong() {
        long val = 0;
        byte[] buf = new byte[8];
        readBuffer(buf);
        for (int a = 7; a >= 0; a--) {
            val = val << 8;
            val = val | (buf[a] & 0xFF);
        }
        return val;
    }
}