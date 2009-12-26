package com.mongodb.protocol.message;

import java.io.ByteArrayOutputStream;

import org.junit.runner.RunWith;

import com.mongodb.protocol.DBTransaction;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

@RunWith(JDaveRunner.class)
public class KillCursorsRequestSpec extends Specification<DBTransaction<Void>> {
	public class WithAny {
		public DBTransaction<Void> create() {
			return new DBTransaction<Void>(new KillCursorsRequest(123L, 125L, 120L), 129);
		}

		public void requestCanBeSerialized() {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			context.send(output);
			specify(output.toByteArray(), does.containExactly(new byte[] {
					48, 0, 0, 0, // message_lenght
					-127, 0, 0, 0, // requestId
					0, 0, 0, 0, // responseTo
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
