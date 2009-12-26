package com.mongodb.protocol.bson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.runner.RunWith;

import com.mongodb.protocol.LittleEndianDataReader;
import com.mongodb.protocol.LittleEndianDataWriter;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

@RunWith(JDaveRunner.class)
public class BSONStringSpec extends Specification<BSONString> {
	public class WithValidInput {
		public BSONString create() {
			return new BSONString();
		}

		public void stringValueCanBeEncodedIntoBson() {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			context.write(new BSONCoders(), "foo", "bar", new LittleEndianDataWriter(out));
			specify(out.toByteArray(), does.containExactly(new byte[] { 2, 'f', 'o', 'o', 0, 0, 0, 0, 4, 'b', 'a', 'r', 0 }));
		}

		public void stringValueCanBeDecodedFromBson() {
			specify(context.decode(new BSONCoders(), new LittleEndianDataReader(new ByteArrayInputStream(new byte[] { 0, 0, 0, 3, 'f', 'o', 'o', 0 }))), does.equal("foo"));
		}
	}
}
