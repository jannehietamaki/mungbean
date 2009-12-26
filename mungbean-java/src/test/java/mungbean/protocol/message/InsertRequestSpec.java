package mungbean.protocol.message;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

import mungbean.protocol.DBTransaction;

@RunWith(JDaveRunner.class)
public class InsertRequestSpec extends Specification<DBTransaction<InsertRequest>> {
	public class WithValidRequest {
		@SuppressWarnings("unchecked")
		public DBTransaction<Void> create() {
			InsertRequest message = new InsertRequest("foozbar.foo", new HashMap<String, Object>() {
				{
					put("foo", "bar");
				}
			});
			return new DBTransaction<Void>(message, 123);
		}

		public void messageCanBeSerializedIntoByteStream() {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			context.send(output);
			specify(output.toByteArray(), does.containExactly(new byte[] {
					50, 0, 0, 0, // message_lenght
					123, 0, 0, 0, // requestId
					0, 0, 0, 0, // responseTo
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
