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

import static mungbean.CollectionUtil.list;
import static mungbean.CollectionUtil.map;
import static mungbean.CollectionUtil.merge;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import mungbean.protocol.DBTransaction;
import mungbean.protocol.bson.MapBSONCoders;
import mungbean.query.Query;
import mungbean.query.QueryBuilder;

import org.junit.runner.RunWith;

@RunWith(JDaveRunner.class)
public class UpdateRequestSpec extends Specification<DBTransaction<NoResponseExpected>> {
    public class WithValidRequest {
        public DBTransaction<NoResponseExpected> create() {
            QueryBuilder selector = new Query().field("foo").is("bar");
            Map<String, Object> document = map("zoo", 5);
            return new DBTransaction<NoResponseExpected>(new UpdateRequest<Map<String, Object>>("foozbar.foo", selector, new UpdateOptionsBuilder(), document, new MapBSONCoders(), new MapBSONCoders()), 126);
        }

        public void updateRequestCanBeSerializedToByteStream() {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            context.sendRequest(output);
            specify(output.toByteArray(), does.containExactly(new byte[] { 
            		68, 0, 0, 0, // message_lenght
                    126, 0, 0, 0, // requestId
                    -1, -1, -1, -1, // responseTo
                    -47, 7, 0, 0, // opCode
                    0, 0, 0, 0, // RESERVED
                    'f', 'o', 'o', 'z', 'b', 'a', 'r', '.', 'f', 'o', 'o', 0, // collectionName
                    0, 0, 0, 0, // flags
                    18, 0, 0, 0, // element_size
                    2, // element_type = string
                    'f', 'o', 'o', 0, // name
                    4, 0, 0, 0, // item_length
                    'b', 'a', 'r', 0, // value
                    0, // eoo
                    14, 0, 0, 0, // element_size
                    16, // element_type = int32
                    'z', 'o', 'o', 0, // name
                    5, 0, 0, 0, // value
                    0 // eoo
                    }));
            specify(output.toByteArray().length, does.equal(68));
        }
    }
    
    public class WithStructuralUpdateRequest {
        @SuppressWarnings("unchecked")
        public DBTransaction<NoResponseExpected> create() {
            QueryBuilder selector = new Query().field("foo").is("bar");
			Map<String, Object> document = merge(map("zoo", "5"), map("list", list("1","2","3")));
            return new DBTransaction<NoResponseExpected>(new UpdateRequest<Map<String, Object>>("foozbar.foo", selector, new UpdateOptionsBuilder(), document, new MapBSONCoders(), new MapBSONCoders()), 126);
        }

        public void updateRequestCanBeSerializedToByteStream() {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            context.sendRequest(output);
            byte bytes[]=new byte[] { 
            		108, 0, 0, 0, // message_lenght
                    126, 0, 0, 0, // requestId
                    -1, -1, -1, -1, // responseTo
                    -47, 7, 0, 0, // opCode
                    0, 0, 0, 0, // RESERVED
                    'f', 'o', 'o', 'z', 'b', 'a', 'r', '.', 'f', 'o', 'o', 0, // collectionName
                    0, 0, 0, 0, // flags
                    18, 0, 0, 0, // element_size
                    02, // element_type = string
                    'f', 'o', 'o', 0, // name
                    4, 0, 0, 0, // item_length
                    'b', 'a', 'r', 0, // value
                    0, // eoo
                    54, 0, 0, 0, // element_size
                    2, // element_type = string
                    'z', 'o', 'o', 0, // name
                    2, 0 ,0 ,0, // content_length
                    '5', 0, // value
                    4, // element_type = array
                    'l','i','s','t',0, // list
                    32,0,0,0, // element_size
                    
                    2, // element_type = string
                    '0',0,
                    2,0,0,0, // element_size=2
                    '1',0, // value 
                    
                    2, // element_type = string
                    '1',0,
                    2,0,0,0, // element_size=2
                    '2',0, // value 
                    
                    2, // element_type = string
                    '2',0,
                    2,0,0,0, // element_size=2
                    '3',0, // value                     
                    0, // eoo
                    0 // eoo
                    };
            specify(output.toByteArray(), does.containExactly(bytes));
            specify(bytes.length, does.equal(108));
            specify(output.toByteArray().length, does.equal(108));
        }
    }
    
}
