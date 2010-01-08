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

import java.security.MessageDigest;

public class Md5 {
    private final MessageDigest algorithm;

    public Md5() {
        try {
            algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String md5(String source) {
        return new Md5().update(source.getBytes(Utils.UTF8)).toHex();
    }

    public String toHex() {
        return Utils.toHex(algorithm.digest());
    }

    public Md5 update(byte[] bytes) {
        algorithm.update(bytes);
        return this;
    }

    public Md5 update(byte[] bytes, int start, int length) {
        algorithm.update(bytes, start, length);
        return this;
    }
}
