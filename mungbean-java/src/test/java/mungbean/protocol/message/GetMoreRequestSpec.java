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
package mungbean.protocol.message;

import java.io.ByteArrayOutputStream;

import org.junit.runner.RunWith;

import mungbean.protocol.DBTransaction;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

@RunWith(JDaveRunner.class)
public class GetMoreRequestSpec extends Specification<DBTransaction<QueryResponse>> {
	public class WithValidRequest {
		public DBTransaction<QueryResponse> create() {
			return new DBTransaction<QueryResponse>(new GetMoreRequest("foozbar.foo", 123123L, 0), 127, -1);
		}

		public void requestCanBeSerialized() {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			context.send(output);
			specify(output.toByteArray(), does.containExactly(new byte[] { 44, 0, 0, 0, // message_lenght
					127, 0, 0, 0, // requestId
					-1, -1, -1, -1, // responseTo
					-43, 7, 0, 0, // opCode
					0, 0, 0, 0, // RESERVED
					'f', 'o', 'o', 'z', 'b', 'a', 'r', '.', 'f', 'o', 'o', 0, // collectionName
					0, 0, 0, 0, // numberToReturn
					-13, -32, 1, 0, 0, 0, 0, 0 // CursorID
					}));
		}
	}
}
