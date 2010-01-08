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

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mungbean.ObjectId;

public class GridFsFile {
    private final ObjectId _id = new ObjectId();
    private final String filename;
    private final String contentType;
    private final Double length;
    private final Double chunkSize = new Double(256 * 1024);
    private final Date uploadDate = new Date();
    private final List<String> aliases = null;
    private final Map<String, Object> metadata = new HashMap<String, Object>();
    private String md5 = null;

    public GridFsFile(File file) {
        this.filename = file.getName();
        this.contentType = "application/octet-stream"; // TODO
        this.length = new Double(file.length());
    }

    public ObjectId id() {
        return _id;
    }

    public String filename() {
        return filename;
    }

    public String contentType() {
        return contentType;
    }

    public int length() {
        return length.intValue();
    }

    public int chunkSize() {
        return chunkSize.intValue();
    }

    public Date uploadDate() {
        return uploadDate;
    }

    public List<String> aliases() {
        return aliases;
    }

    public Map<String, Object> metadata() {
        return metadata;
    }

    public String md5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;

    }
}
