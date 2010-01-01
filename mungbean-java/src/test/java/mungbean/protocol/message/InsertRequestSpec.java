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

import static mungbean.CollectionUtil.map;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import mungbean.protocol.DBTransaction;
import mungbean.protocol.bson.MapBSONCoders;

import org.junit.runner.RunWith;

@RunWith(JDaveRunner.class)
public class InsertRequestSpec extends Specification<DBTransaction<NoResponseExpected>> {
	public class WithValidRequest {
		@SuppressWarnings("unchecked")
		public DBTransaction<NoResponseExpected> create() {
			InsertRequest<Map<String, Object>> message = new InsertRequest<Map<String, Object>>("foozbar.foo", new MapBSONCoders(), map("foo", "bar"));
			return new DBTransaction<NoResponseExpected>(message, 123);
		}

		public void messageCanBeSerializedIntoByteStream() {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			context.sendRequest(output);
			specify(output.toByteArray(), does.containExactly(new byte[] { 
					50, 0, 0, 0, // message_lenght
					123, 0, 0, 0, // requestId
					-1, -1, -1, -1, // responseTo
					-46, 7, 0, 0, // opCode
					0, 0, 0, 0, // RESERVED
					'f', 'o', 'o', 'z', 'b', 'a', 'r', '.', 'f', 'o', 'o', 0, // collectionName
					18, 0, 0, 0, // element_size
					2, // element_type = string
					'f', 'o', 'o', 0, // name
					4, 0, 0, 0, // item_length
					'b', 'a', 'r', 0, // value
					0 // eoo
					}));
		}
	}

}
