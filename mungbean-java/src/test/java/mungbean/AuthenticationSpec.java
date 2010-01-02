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
import java.io.InputStream;
import java.util.LinkedHashMap;

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import mungbean.protocol.DBConnection;

import org.junit.runner.RunWith;

@RunWith(JDaveRunner.class)
public class AuthenticationSpec extends Specification<Authentication> {
    public class WithValidPassword {
        public Authentication create() {
            return new Authentication("projectx", "joe", "passwordForJoe");
        }

        public void validAuthenticationRequestCanBeGenerated() {
            LinkedHashMap<String, Object> request = context.authenticationRequest("6c585a7907bbf660");
            specify(request.get("key"), does.equal("f5f547d7c1ce7d18149cf714feb5f4cb"));
        }
    }

    public class WithFullTransaction {
        public Authentication create() {
            return new Authentication("projectx", "joe", "passwordForJoe");
        }

        public void generatedBinaryStreamIsValid() {
            byte[] nonceResponse = new byte[] { 81, 0, 0, 0, // length
                    0, 0, 0, 2,// reqid
                    0, 0, 0, 1, // responseTo
                    01, 0, 0, 0, // opCode =OP_REPLY
                    0, 0, 0, 0, // responseFlag
                    0, 0, 0, 0, 0, 0, 0, 0, // cursorId
                    0, 0, 0, 0, // startingFrom
                    1, 0, 0, 0, // numberReturned
                    46, 0, 0, 0, // bson_length
                    2, // string
                    'n', 'o', 'n', 'c', 'e', 0, // 'nonce'
                    17, 0, 0, 0, // data_length
                    '7', 'd', 'a', '2', '7', 'b', '1', '4', '4', 'd', 'a', 'c', '0', '9', 'f', '0', 0, // nonce
                    1, // data_number
                    'o', 'k', 0, // 'ok'
                    0, 0, 0, 0, 0, 0, -16, 63, // 1
                    0,// eoo

                    // Authentication response
                    31, 0, 0, 0, // length
                    0, 0, 0, 2,// reqid
                    0, 0, 0, 1, // responseTo
                    01, 0, 0, 0, // opCode =OP_REPLY
                    0, 0, 0, 0, // responseFlag
                    0, 0, 0, 0, 0, 0, 0, 0, // cursorId
                    0, 0, 0, 0, // startingFrom
                    1, 0, 0, 0, // numberReturned
                    12, 0, 0, 0, // bson_length
                    1, // data_number
                    'o', 'k', 0, // 'ok'
                    0, 0, 0, 0, 0, 0, -16, 63, // 1
                    0 // eoo

            };
            InputStream input = new ByteArrayInputStream(nonceResponse);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            context.authenticate(new DBConnection(input, output));
            specify(output.toByteArray(), does.containExactly(new byte[] { //					
                    65, 0, 0, 0, // message_length
                            1, 0, 0, 0, // requestID
                            -1, -1, -1, -1, // responseTo
                            -44, 7, 0, 0, // opCode
                            0, 0, 0, 0, // oopts
                            'p', 'r', 'o', 'j', 'e', 'c', 't', 'x', '.', '$', 'c', 'm', 'd', 0, // FullCollectionName
                            0, 0, 0, 0, // numberToSkip
                            -1, -1, -1, -1, // numberToReturn
                            23, 0, 0, 0, // ObjSize
                            1, // number
                            103, 101, 116, 110, 111, 110, 99, 101, 0, // name
                            0, 0, 0, 0, 0, 0, -16, 63, // 1
                            0, // EOO

                            -103, 0, 0, 0, // message_length
                            2, 0, 0, 0, // requestId
                            -1, -1, -1, -1, // responseTo
                            -44, 7, 0, 0, // opCode
                            0, 0, 0, 0, // opts
                            'p', 'r', 'o', 'j', 'e', 'c', 't', 'x', '.', '$', 'c', 'm', 'd', 0, // FullCollectionName
                            0, 0, 0, 0, // numberToSkip
                            -1, -1, -1, -1, // numberToReturn
                            111, 0, 0, 0, // objSize
                            1, // number
                            'a', 'u', 't', 'h', 'e', 'n', 't', 'i', 'c', 'a', 't', 'e', 0, // "authenticate"
                            0, 0, 0, 0, 0, 0, -16, 63, // 1
                            2, // string
                            'u', 's', 'e', 'r', 0, // "user"
                            4, 0, 0, 0, // size
                            'j', 'o', 'e', 0, // "joe"
                            2, // string
                            'n', 'o', 'n', 'c', 'e', 0, // nonce
                            17, 0, 0, 0, // length
                            '7', 'd', 'a', '2', '7', 'b', '1', '4', '4', 'd', 'a', 'c', '0', '9', 'f', '0', 0, // nonce
                            2, // string
                            'k', 'e', 'y', 0, // key
                            33, 0, 0, 0, // length
                            'f', 'd', '6', 'b', 'a', '0', 'a', '3', 'f', 'c', '1', '2', 'a', '2', '4', '0', '1', '8', '0', '0', 'f', '1', '2', '5', '1', '1', 'd', '2', 'b', '0', 'a', '0', 0, // key
                            0 // EOO
                    }));
        }
    }
}
