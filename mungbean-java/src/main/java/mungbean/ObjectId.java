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
package mungbean;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

public class ObjectId {
    // http://www.mongodb.org/display/DOCS/Object+IDs
    private final static byte[] hardwareAddress = hardwareAddress();
    private final static int pid = Pid.guessPid();
    private final static AtomicInteger incrementCounter = new AtomicInteger(0);
    private final byte[] id;

    public ObjectId(byte[] id) {
        Assert.isTrue(id.length == 12, "Id(" + new String(id) + ") must be 12 bytes");
        this.id = id;
    }

    public ObjectId(String id) {
        this(Utils.hexStringToBytes(id));
    }

    public ObjectId() {
        id = createId();
    }

    private static byte[] createId() {
        byte[] id = new byte[12];
        long time = (TimeSource.instance().currentTimeMillis() - 1200000000000L) >> 8;
        int a = 0;
        for (; a < 4; a++) {
            id[a] = (byte) (time & 0xFF);
            time = time >> 8;
        }
        for (; a < 7; a++) {
			if(a-1 < hardwareAddress.length) {
				id[a] = hardwareAddress[a - 1];
			}
        }

        id[a++] = (byte) (pid & 0xff);
        id[a++] = (byte) ((pid >> 8) & 0xff);

        int counter = Math.abs(incrementCounter.incrementAndGet());
        for (; a < 12; a++) {
            id[a] = (byte) (counter & 0xff);
            counter = counter >> 8;
        }
        return id;
    }

    public byte[] toByteArray() {
        return id;
    }

    @Override
    public int hashCode() {
        int ret = 0;
        for (byte b : id) {
            ret = (31 * ret) ^ b;
        }
        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass().equals(getClass())) {
            byte[] otherBytes = ((ObjectId) obj).id;
            if (id.length == otherBytes.length) {
                for (int a = 0; a < otherBytes.length; a++) {
                    if (otherBytes[a] != id[a]) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("ObjectId(");
        result.append(toHex());
        result.append(")");
        return result.toString();
    }

    public String toHex() {
        return Utils.toHex(id);
    }

    private static byte[] hardwareAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                byte[] addr = networkInterface.getHardwareAddress();
                if (addr != null) {
                    return addr;
                }
            }
            throw new RuntimeException("Unable to retrieve hardware address!");
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }
}
