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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import mungbean.ListQueryCallback;
import mungbean.protocol.DBTransaction;
import mungbean.protocol.bson.BSONMap;
import mungbean.protocol.bson.MapBSONCoders;
import mungbean.query.Query;

import org.junit.runner.RunWith;

@RunWith(JDaveRunner.class)
public class QueryRequestSpec extends Specification<DBTransaction<QueryResponse<Map<String, Object>>>> {

    public class WithoutQueryRules {
        public DBTransaction<QueryResponse<Map<String, Object>>> create() {
            QueryRequest<Map<String, Object>> message = new QueryRequest<Map<String, Object>>("foozbar.foo", new QueryOptionsBuilder(), new Query(), new MapBSONCoders(), new BSONMap());
            return new DBTransaction<QueryResponse<Map<String, Object>>>(message, 124);
        }

        public void messageCanBeSerializedIntoByteStream() {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            context.sendRequest(output);
            specify(output.toByteArray(), does.containExactly(new byte[] { //
                    45, 0, 0, 0, // message_lenght
                            124, 0, 0, 0, // requestID
                            -1, -1, -1, -1, // responseTo
                            -44, 7, 0, 0, // opCode
                            0, 0, 0, 0, // opts
                            'f', 'o', 'o', 'z', 'b', 'a', 'r', '.', 'f', 'o', 'o', 0, // fullCollectionName
                            00, 00, 00, 00, // numberToSkip
                            00, 00, -120, 19, // NumberToReturn
                            05, 00, 00, 00, // ObjSize
                            00 // EOO
                    }));
        }
    }

    public class WithQueryContaingingRules {
        public DBTransaction<QueryResponse<Map<String, Object>>> create() {
            QueryRequest<Map<String, Object>> message = new QueryRequest<Map<String, Object>>("foozbar.foo", new QueryOptionsBuilder().slaveOk(), new Query().setLimit(10).field("foo").is("bar"), new MapBSONCoders(), new BSONMap());
            return new DBTransaction<QueryResponse<Map<String, Object>>>(message, 124);
        }

        public void messageCanBeSerializedIntoByteStream() {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            context.sendRequest(output);
            specify(output.toByteArray(), does.containExactly(new byte[] { 58, 0, 0, 0, // messageLength
                    124, 0, 0, 0, // requestID
                    -1, -1, -1, -1, // responseTo
                    -44, 7, 0, 0, // opCode
                    4, 0, 0, 0, // opts
                    'f', 'o', 'o', 'z', 'b', 'a', 'r', '.', 'f', 'o', 'o', 0, // fullCollectionName
                    0, 0, 0, 0, // numberToSkip
                    -10, -1, -1, -1, // NumberToReturn = -10
                    // query BSON
                    18, 0, 0, 0, // obj_size
                    2, // element type = string
                    'f', 'o', 'o', 0,// element name
                    4, 0, 0, 0, // data_size
                    'b', 'a', 'r', 0, // element_data
                    0 // eoo
                    }));
        }

        public void responseCanBeDeserializedFromByteStream() {
            ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 49, 0, 0, 0, // messageLength
                    125, 0, 0, 0, // requestID
                    124, 0, 0, 0, // responseTo
                    1, 0, 0, 0, // opCode
                    0, 0, 0, 0, // responseFlag
                    1, 0, 0, 0, 0, 0, 0, 0, // cursorID
                    0, 0, 0, 0, // startingFrom
                    1, 0, 0, 0, // numberReturned
                    14, 0, 0, 0, // obj_size
                    2, // element_type = string
                    'f', 'o', 'o', 0, // name
                    8, 0, 0, 0, // data_size
                    'b', 'a', 'r', 0, // value
                    0 // eoo
                    });
            QueryResponse<Map<String, Object>> response = context.readResponse(input);
            ListQueryCallback<Map<String, Object>> callback = new ListQueryCallback<Map<String, Object>>();
            response.readResponse(callback);
            specify(response.opCode(), does.equal(RequestOpCode.OP_REPLY));
            specify(callback.values().get(0), does.equal(map("foo", "bar")));
        }
    }
}
