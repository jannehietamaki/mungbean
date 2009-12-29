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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import mungbean.protocol.command.admin.IndexOptionsBuilder;

import org.junit.runner.RunWith;

@RunWith(JDaveRunner.class)
public class CollectionAdminSpec extends Specification<CollectionAdmin> {
	private final ByteArrayOutputStream output = new ByteArrayOutputStream();
	private final ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);

	MapDBCollection collection = new MapDBCollection(new TestDbOperationExecutor(input, output), "dbName", "collectionName");

	public class WhenCreatingIndex {
		public CollectionAdmin create() {
			return new CollectionAdmin(collection);
		}

		public void messageIsSerializedToByteStream() {
			context.ensureIndex(new String[] { "foo" }, new IndexOptionsBuilder().dropDups().unique());
			specify(output.toByteArray().length, does.equal(153));
		}
	}
}
