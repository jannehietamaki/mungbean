package mungbean.protocol.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

import mungbean.protocol.DBTransaction;
import mungbean.protocol.message.QueryOptionsBuilder;
import mungbean.protocol.message.QueryRequest;
import mungbean.protocol.message.QueryResponse;
import mungbean.protocol.message.RequestOpCode;

@RunWith(JDaveRunner.class)
public class QueryRequestSpec extends Specification<DBTransaction<QueryResponse>> {

	public class WithoutQueryRules {
		public DBTransaction<QueryResponse> create() {
			QueryRequest message = new QueryRequest("foozbar.foo", new QueryOptionsBuilder(), 0, 0, new HashMap<String, Object>());
			return new DBTransaction<QueryResponse>(message, 124);
		}

		public void messageCanBeSerializedIntoByteStream() {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			context.send(output);
			specify(output.toByteArray(), does.containExactly(new byte[] { 
					45, 0, 0, 0, // message_lenght
					124, 0, 0, 0, // requestID
					0, 0, 0, 0, // responseTo
					-44, 7, 0, 0, // opCode
					0, 0, 0, 0, // opts
					'f', 'o', 'o', 'z', 'b', 'a', 'r', '.', 'f', 'o', 'o', 0, // fullCollectionName
					00, 00, 00, 00, // numberToSkip
					00, 00, 00, 00, // NumberToReturn
					05, 00, 00, 00, // ObjSize
					00 // EOO
					}));
		}
	}

	public class WithQueryContaingingRules {
		public DBTransaction<QueryResponse> create() {
			QueryRequest message = new QueryRequest("foozbar.foo", new QueryOptionsBuilder().slaveOk(), 0, 0, new HashMap<String, Object>() {
				{
					put("foo", "bar");
				}
			});
			return new DBTransaction<QueryResponse>(message, 124);
		}

		public void messageCanBeSerializedIntoByteStream() {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			context.send(output);
			specify(output.toByteArray(), does.containExactly(new byte[] { 
					58, 0, 0, 0, // messageLength
					124, 0, 0, 0, // requestID
					0, 0, 0, 0, // responseTo
					-44, 7, 0, 0, // opCode
					4, 0, 0, 0, // opts
					'f', 'o', 'o', 'z', 'b', 'a', 'r', '.', 'f', 'o', 'o', 0, // fullCollectionName
					0, 0, 0, 0, // numberToSkip
					0, 0, 0, 0, // numberToReturn
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
			ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 
					49, 0, 0, 0, // messageLength
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
			QueryResponse response = context.readResponse(input);
			specify(response.opCode(), does.equal(RequestOpCode.OP_REPLY));
			specify(response.values().get(0), does.equal(new HashMap<String, Object>() {
				{
					put("foo", "bar");
				}
			}));
		}
	}
}
