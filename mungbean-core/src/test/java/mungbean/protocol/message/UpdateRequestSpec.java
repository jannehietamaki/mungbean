package com.mongodb.protocol.message;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

import com.mongodb.protocol.DBTransaction;

@RunWith(JDaveRunner.class)
public class UpdateRequestSpec extends Specification<DBTransaction<Void>> {
	public class WithValidRequest {
		public DBTransaction<Void> create() {
			Map<String, Object> selector = new HashMap<String, Object>() {
				{
					put("foo", "bar");
				}
			};
			Map<String, Object> document = new HashMap<String, Object>() {
				{
					put("zoo", 5);
				}
			};
			return new DBTransaction<Void>(new UpdateRequest("foozbar.foo", new UpdateOptionsBuilder(), selector, document), 126);
		}

		public void updateRequestCanBeSerializedToByteStream() {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			context.send(output);
			specify(output.toByteArray(), does.containExactly(new byte[] { 
					68, 0, 0, 0, // message_lenght
					126, 0, 0, 0, // requestId
					0, 0, 0, 0, // responseTo
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
		}
	}
}
