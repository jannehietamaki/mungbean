/*
   Copyright 2009-2010 Janne Hietamäki

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

package mungbean.gridfs;

import java.io.IOException;
import java.io.OutputStream;

import mungbean.Md5;
import mungbean.protocol.RuntimeIOException;

public class Md5Writer {
    private final OutputStream out;
    private final Md5 md5 = new Md5();

    public Md5Writer(OutputStream out) {
        this.out = out;
    }

    public void write(byte[] data) {
        try {
            md5.update(data);
            out.write(data);
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    public String md5() {
        return md5.toHex();
    }

}
