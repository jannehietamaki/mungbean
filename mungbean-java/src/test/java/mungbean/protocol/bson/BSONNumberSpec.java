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
package mungbean.protocol.bson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.runner.RunWith;

import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

@RunWith(JDaveRunner.class)
public class BSONNumberSpec extends Specification<BSONNumber> {
	public class WithValidInput {
		public BSONNumber create() {
			return new BSONNumber();
		}

		public void doubleValueCanBeEncodedIntoBson() {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			context.write(new BSONCoders(), "foo", 123D, new LittleEndianDataWriter(out));
			specify(out.toByteArray(), does.containExactly(new byte[] { 1, 'f', 'o', 'o', 0, 0, 0, 0, 0, 0, -64, 94, 64 }));
		}

		public void doubleValueCanBeDecodedFromBson() {
			specify(context.decode(null, new LittleEndianDataReader(new ByteArrayInputStream(new byte[] { 0, 0, 0, 0, 0, -64, 94, 64 }))), does.equal(123D));
		}
	}
}
