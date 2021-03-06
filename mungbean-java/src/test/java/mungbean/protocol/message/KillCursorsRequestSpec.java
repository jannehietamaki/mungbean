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
public class KillCursorsRequestSpec extends Specification<DBTransaction<NoResponseExpected>> {
    public class WithAny {
        public DBTransaction<NoResponseExpected> create() {
            return new DBTransaction<NoResponseExpected>(new KillCursorsRequest(123L, 125L, 120L), 129);
        }

        public void requestCanBeSerialized() {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            context.sendRequest(output);
            specify(output.toByteArray(), does.containExactly(new byte[] { 48, 0, 0, 0, // message_lenght
                    -127, 0, 0, 0, // requestId
                    -1, -1, -1, -1, // responseTo
                    -41, 7, 0, 0, // opCode
                    0, 0, 0, 0, // RESERVED
                    3, 0, 0, 0, // numberOfCursorIDs
                    123, 0, 0, 0, 0, 0, 0, 0, // id1
                    125, 0, 0, 0, 0, 0, 0, 0, // id2
                    120, 0, 0, 0, 0, 0, 0, 0 // id3
                    }));
        }
    }

}
