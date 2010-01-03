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

import java.nio.charset.Charset;

import sun.misc.HexDumpEncoder;

public class Utils {

    public static final Charset UTF8 = Charset.forName("UTF-8");

    public static String toHex(byte[] content) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : content) {
            hexString.append(String.format("%02x", (0xFF & b)));
        }
        return hexString.toString().toLowerCase();
    }

    public static byte[] hexStringToBytes(String input) {
        int len = input.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(input.charAt(i), 16) << 4) + Character.digit(input.charAt(i + 1), 16));
        }
        return data;
    }

    public static String hexDump(byte[] bytes) {
        return new HexDumpEncoder().encode(bytes);
    }
}