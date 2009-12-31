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
import java.util.Map;

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import mungbean.protocol.DBTransaction;

import org.junit.runner.RunWith;

@RunWith(JDaveRunner.class)
public class CommandRequestSpec extends Specification<DBTransaction<Map<String, Object>>> {
	public class WithValidCommandQuery {
		public DBTransaction<Map<String, Object>> create() {
			return new DBTransaction<Map<String, Object>>(new CommandRequest("foobar", "getlasterror"), 123);
		}

		public void commandQueryCanBeSerialized() {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			context.sendRequest(output);
			specify(output.toByteArray(), does.containExactly(new byte[] { 67, 0, 0, 0, // message_lenght
					123, 0, 0, 0, // requestID
					-1, -1, -1, -1, // responseTo
					-44, 7, 0, 0, // opCode
					0, 0, 0, 0, // opts
					'f', 'o', 'o', 'b', 'a', 'r', '.', '$', 'c', 'm', 'd', 0, // fullCollectionName
					0, 0, 0, 0, // numberToSkip
					-1, -1, -1, -1, // NumberToReturn
					27, 0, 0, 0, // ObjSize
					01, // data_type = number
					'g', 'e', 't', 'l', 'a', 's', 't', 'e', 'r', 'r', 'o', 'r', 0, // command
					0, 0, 0, 0, 0, 0, -16, 63, // value == 1 (double)
					0 // EOO
					}));
		}
	}

}
