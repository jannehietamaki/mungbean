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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import mungbean.Mungbean;
import mungbean.ObjectId;
import mungbean.Server;

import org.junit.runner.RunWith;

@RunWith(JDaveRunner.class)
public class GridFsStorageIntegrationTest extends Specification<GridFsStorage> {
    public class WithStorage {

        public GridFsStorage create() {
            return new Mungbean(new Server("localhost", 27017)).openDatabase(new ObjectId().toHex()).openStorage("foobar");
        }

        public void fileCanBeStoredIntoStorage() throws IOException {
            File sourceFile = File.createTempFile("foo", ".txt");
            OutputStream out = new FileOutputStream(sourceFile);
            out.write("foobar".getBytes());
            out.close();
            context.storeFile(sourceFile);

            sourceFile.delete();
            context.retrieveFile(sourceFile.getName(), sourceFile);
            specify(sourceFile.length(), does.equal(6));
        }
    }
}
