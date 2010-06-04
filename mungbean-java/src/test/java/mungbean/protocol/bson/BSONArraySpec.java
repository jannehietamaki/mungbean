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
import java.util.Arrays;
import java.util.List;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;

@RunWith(JDaveRunner.class)
public class BSONArraySpec extends Specification<BSONArray<Integer>> {
    public class WithList {
        public BSONArray<Integer> create() {
            return new BSONArray<Integer>();
        }

        public void listWithIntegersCanBeEncodedIntoBson() {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            List<Integer> input = Arrays.asList(1, 2);
            LittleEndianDataWriter leWriter = new LittleEndianDataWriter(out);
            context.encode(new MapBSONCoders(), input, leWriter);
            specify(out.toByteArray(), does.containExactly(new byte[] { 19, 0, 0, 0, 16, 48, 0, 1, 0, 0, 0, 16, 49, 0, 2, 0, 0, 0, 0 }));
        }

        public void bsonCanBeDecodedIntoListOfIntegers() {
            ByteArrayInputStream in = new ByteArrayInputStream(new byte[] { 18, 0, 0, 0, 16, 48, 0, 1, 0, 0, 0, 16, 49, 0, 2, 0, 0, 0, 0 });
            List<?> ret = context.decode(new MapBSONCoders(), new LittleEndianDataReader(in));
            specify(ret, containsExactly(1, 2));
        }
    }
}
