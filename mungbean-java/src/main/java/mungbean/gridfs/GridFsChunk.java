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

import mungbean.ObjectId;

public class GridFsChunk {
    private final ObjectId _id = new ObjectId();
    private final ObjectId files_id;
    private final Double n;
    private final byte[] data;

    public GridFsChunk(GridFsFile file, byte[] data, int n) {
        this.data = data;
        this.n = new Integer(n).doubleValue();
        this.files_id = file.id();
    }

    public byte[] data() {
        return data;
    }

    public ObjectId id() {
        return _id;
    }

    public ObjectId filesId() {
        return files_id;
    }

    public int n() {
        return n.intValue();
    }
}
