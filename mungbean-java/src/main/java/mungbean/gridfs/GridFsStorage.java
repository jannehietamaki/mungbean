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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import mungbean.Assert;
import mungbean.DBCollection;
import mungbean.NotFoundException;
import mungbean.protocol.RuntimeIOException;
import mungbean.query.Query;

public class GridFsStorage {
    private final DBCollection<GridFsFile> fileCollection;
    private final DBCollection<GridFsChunk> chunkCollection;

    public GridFsStorage(DBCollection<GridFsFile> fileCollection, DBCollection<GridFsChunk> chunkCollection) {
        this.fileCollection = fileCollection;
        this.chunkCollection = chunkCollection;
    }

    public void retrieveFile(String fileName, File target) {
        try {
            FileOutputStream out = new FileOutputStream(target);
            retrieveFile(fileName, out);
            out.close();
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    private void retrieveFile(String fileName, OutputStream out) {
        GridFsFile file = findFile(fileName);
        Query query = new Query();
        query.field("n").orderAscending();
        query.field("files_id").is(file.id());
        Md5Writer writer = new Md5Writer(out);
        for (GridFsChunk chunk : chunkCollection.query(query)) {
            writer.write(chunk.data());
        }
        Assert.isTrue(writer.md5().equalsIgnoreCase(file.md5()), "Invalid checksum " + writer.md5() + "!=" + file.md5());
    }

    public void storeFile(File sourceFile) {
        try {
            // TODO: How we should handle existing files / chunks when
            // overwriting with the same name
            GridFsFile file = new GridFsFile(sourceFile);
            FileInputStream input = new FileInputStream(sourceFile);
            byte buffer[] = new byte[file.chunkSize()];
            int bytes;
            int n = 0;
            Md5Reader reader = new Md5Reader(input);
            while ((bytes = reader.read(buffer)) != -1) {
                byte[] bytesToStore = new byte[bytes];
                System.arraycopy(buffer, 0, bytesToStore, 0, bytes);
                chunkCollection.save(new GridFsChunk(file, bytesToStore, n));
                n++;
            }
            file.setMd5(reader.md5());
            fileCollection.save(file);
            input.close();
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    public void removeFile(String filename) {
        GridFsFile file = findFile(filename);
        fileCollection.delete(file.id());
        chunkCollection.delete(new Query().field("file_id").is(file.id()));
    }

    private GridFsFile findFile(String fileName) {
        List<GridFsFile> files = fileCollection.query(new Query().field("filename").is(fileName));
        if (files.isEmpty()) {
            throw new NotFoundException("File not found: " + fileName);
        }
        return files.get(0);
    }
}
