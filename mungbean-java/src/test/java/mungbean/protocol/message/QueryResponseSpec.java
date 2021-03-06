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

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import mungbean.ListQueryCallback;
import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.bson.BSONMap;
import mungbean.protocol.bson.MapBSONCoders;

import org.junit.runner.RunWith;

@RunWith(JDaveRunner.class)
public class QueryResponseSpec extends Specification<List<Map<String, Object>>> {
    public class WithCommandResponse {
        public List<Map<String, Object>> create() {
            byte[] bytes = new byte[] { //
            65, 0, 0, 0, // length
                    70, 71, 94, 07,// reqid
                    12, 34, 32, 36, // responseTo
                    01, 0, 0, 0, // opCode =OP_REPLY
                    0, 0, 0, 0, // responseFlag
                    0, 0, 0, 0, 0, 0, 0, 0, // cursorId
                    0, 0, 0, 0, // startingFrom
                    01, 0, 0, 0, // numberReturned
                    29, 0, 0, 0, // bson_length
                    10, // null
                    'e', 'r', 'r', 0, // 'err'
                    16, // data_int
                    'n', 0, // 'n'
                    0, 0, 0, 0, // 0
                    01, // data_number
                    'o', 'k', 0, // 'ok'
                    0, 0, 0, 0, 0, 0, -16, 63, // 1
                    0 // eoo
            };
            ListQueryCallback<Map<String, Object>> callback = new ListQueryCallback<Map<String, Object>>();
            QueryResponse<Map<String, Object>> response = new QueryResponse<Map<String, Object>>(new LittleEndianDataReader(new ByteArrayInputStream(bytes)), new BSONMap(), new MapBSONCoders());
            response.readResponse(callback);
            return callback.values();
        }

        public void responseCanBeParsed() {
            specify(context.size(), does.equal(1));
            Map<String, Object> obj = context.get(0);
            specify(obj.get("err"), does.equal(null));
            specify(obj.get("n"), does.equal(0));
            specify(obj.get("ok"), does.equal(1D));
        }
    }
}
