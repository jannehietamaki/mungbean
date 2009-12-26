package com.mongodb.protocol.bson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

import com.mongodb.protocol.LittleEndianDataReader;
import com.mongodb.protocol.LittleEndianDataWriter;

@RunWith(JDaveRunner.class)
public class BSONArraySpec extends Specification<BSONArray> {
	public class WithList {
		public BSONArray create() {
			return new BSONArray();
		}

		public void listWithIntegersCanBeEncodedIntoBson() {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			List<Integer> input = Arrays.asList(1, 2);
			LittleEndianDataWriter leWriter = new LittleEndianDataWriter(out);
			context.encode(new BSONCoders(), input, leWriter);
			specify(out.toByteArray(), does.containExactly(new byte[] { 18, 0, 0, 0, 16, 48, 0, 1, 0, 0, 0, 16, 49, 0, 2, 0, 0, 0, 0 }));
		}

		public void bsonCanBeDecodedIntoListOfIntegers() {
			ByteArrayInputStream in = new ByteArrayInputStream(new byte[] { 18, 0, 0, 0, 16, 48, 0, 1, 0, 0, 0, 16, 49, 0, 2, 0, 0, 0, 0 });
			List<?> ret = context.decode(new BSONCoders(), new LittleEndianDataReader(in));
			specify(ret, containsExactly(1, 2));
		}
	}
}
