package com.mongodb.protocol.message;

import java.io.ByteArrayOutputStream;

import org.junit.runner.RunWith;

import com.mongodb.protocol.DBTransaction;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

@RunWith(JDaveRunner.class)
public class GetMoreRequestSpec extends Specification<DBTransaction<QueryResponse>> {
	public class WithValidRequest {
		public DBTransaction<QueryResponse> create() {
			return new DBTransaction<QueryResponse>(new GetMoreRequest("foozbar.foo", 123123L, 0), 127);
		}

		public void requestCanBeSerialized() {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			context.send(output);
			specify(output.toByteArray(), does.containExactly(new byte[] {
					44, 0, 0, 0, // message_lenght
					127, 0, 0, 0, // requestId
					0, 0, 0, 0, // responseTo
					-43, 7, 0, 0, // opCode
					0, 0, 0, 0,  // RESERVED 
					'f', 'o', 'o', 'z', 'b', 'a', 'r', '.', 'f', 'o', 'o', 0, // collectionName
					 0, 0, 0, 0, // numberToReturn
					 -13, -32, 1, 0, 0, 0, 0, 0 // CursorID 
			}));
		}
	}
}
