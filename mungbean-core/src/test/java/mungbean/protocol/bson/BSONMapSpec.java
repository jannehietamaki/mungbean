package com.mongodb.protocol.bson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.runner.RunWith;

import com.mongodb.protocol.LittleEndianDataReader;
import com.mongodb.protocol.LittleEndianDataWriter;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

@RunWith(JDaveRunner.class)
public class BSONMapSpec extends Specification<BSONMap> {
	private static final byte[] MAP_FIXTURE = new byte[] { 3, 'f', 'o', 'o', 0, 22, 0, 0, 0, 16, 'f', 'o', 'o', 0, 1, 0, 0, 0, 2, 'b', 'a', 'r', 0, 4, 0, 0, 0, 'z', 'a', 'p', 0, 0 };

	public class WithValidInput {

		public BSONMap create() {
			return new BSONMap();
		}

		public void mapCanBeEncodedIntoBson() {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map<String, Object> map = new HashMap<String, Object>() {
				{
					put("foo", 1);
					put("bar", "zap");
				}
			};
			context.write(new BSONCoders(), "foo", map, new LittleEndianDataWriter(out));
			specify(out.toByteArray(), does.containExactly(MAP_FIXTURE));
		}

		public void mapCanBeDecodedFromBson() {
			LittleEndianDataReader reader = new LittleEndianDataReader(new ByteArrayInputStream(MAP_FIXTURE));
			specify(reader.readByte(), does.equal((byte) 3));
			specify(context.readPath(reader, ""), does.equal("foo"));
			Map<String, Object> ret = context.read(new BSONCoders(), reader);
			specify(ret.get("foo"), does.equal(1));
			specify(ret.get("bar"), does.equal("zap"));
		}
	}

}
