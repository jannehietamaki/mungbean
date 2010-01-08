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

import java.util.Map;

import mungbean.gridfs.GridFsChunk;
import mungbean.gridfs.GridFsFile;
import mungbean.gridfs.GridFsStorage;
import mungbean.pojo.PojoDBCollection;
import mungbean.protocol.command.admin.IndexOptionsBuilder;

public class Database extends AbstractDatabase {
    public Database(DBOperationExecutor executor, String name) {
        super(executor, name);
    }

    public DBCollection<Map<String, Object>> openCollection(String name) {
        return mapCollection(name);
    }

    public <T> DBCollection<T> openCollection(String name, Class<T> type) {
        return new PojoDBCollection<T>(executor(), dbName(), name, type);
    }

    public <T> DBCollection<T> openStorageCollection(String name, Class<T> type) {
        return new PojoDBCollection<T>(executor(), dbName(), name, type);
    }

    public GridFsStorage openStorage(String name) {
        DBCollection<GridFsFile> storageCollection = openStorageCollection(name + ".files", GridFsFile.class);
        storageCollection.collectionAdmin().ensureIndex(new IndexOptionsBuilder().field("files_id").field("n"));
        GridFsStorage storage = new GridFsStorage(storageCollection, openStorageCollection(name + ".chunks", GridFsChunk.class));
        return storage;
    }
}
