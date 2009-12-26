package mungbean.protocol.bson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.runner.RunWith;

import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

@RunWith(JDaveRunner.class)
public class BSONBooleanSpec extends Specification<BSONBoolean> {
	public class WhenEncoding {
		public BSONBoolean create() {
			return new BSONBoolean();
		}

		public void trueBooleanCanBeEncodedIntoBson() {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			context.write(new BSONCoders(), "foo", true, new LittleEndianDataWriter(out));
			specify(out.toByteArray(), does.containExactly(new byte[] { 8, 'f', 'o', 'o', 0, 1 }));
		}

		public void falseBooleanCanBeEncodedIntoBson() {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			context.write(new BSONCoders(), "foo", false, new LittleEndianDataWriter(out));
			specify(out.toByteArray(), does.containExactly(new byte[] { 8, 'f', 'o', 'o', 0, 0 }));
		}
	}

	public class WhenDecoding {
		public BSONBoolean create() {
			return new BSONBoolean();
		}

		public void trueBooleanCanBeDecodedFromBson() {
			specify(context.decode(null, new LittleEndianDataReader(new ByteArrayInputStream(new byte[] { 1 }))), does.equal(true));
		}
	}
}
