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
import java.util.HashMap;
import java.util.Map;

import org.junit.runner.RunWith;

import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

@RunWith(JDaveRunner.class)
public class BSONMapSpec extends Specification<BSONMap> {
    public class WithValidInput {
        private final byte[] MAP_FIXTURE = new byte[] { 3, //
                'f', 'o', 'o', 0, // 
                27, 0, 0, 0, //
                16, //
                'f', 'o', 'o', 0, // 
                1, 0, 0, 0, //
                2, //
                'b', 'a', 'r', 0, // 
                4, 0, 0, 0, //
                'z', 'a', 'p', 0, // 
                0 };

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
            context.write(new MapBSONCoders(), "foo", map, new LittleEndianDataWriter(out));
            specify(out.toByteArray(), does.containExactly(MAP_FIXTURE));
        }

        public void mapCanBeDecodedFromBson() {
            LittleEndianDataReader reader = new LittleEndianDataReader(new ByteArrayInputStream(MAP_FIXTURE));
            specify(reader.readByte(), does.equal((byte) 3));
            specify(context.readPath(reader, ""), does.equal("foo"));
            Map<String, Object> ret = context.read(new MapBSONCoders(), reader);
            specify(ret.get("foo"), does.equal(1));
            specify(ret.get("bar"), does.equal("zap"));
        }
    }

    public class WithStructuredData {
        private final byte[] MAP_FIXTURE = new byte[] {// 
        3, // type = map
                'f', 'o', 'o', 0, // 'foo'
                85, 0, 0, 0, // content size
                16, // data int
                'f', 'o', 'o', 0, // 'foo'
                1, 0, 0, 0, // 1
                3, // map
                'b', 'a', 'r', 0, // 'bar'
                66, 0, 0, 0, // content size
                16, // type=int
                'z', 'o', 'o', 0, // 'zoo'
                2, 0, 0, 0, // value=2
                4, // type = array
                'b', 'r', 'r', 0, // 'brr'
                46, 0, 0, 0, // content size
                16, // data_int
                '0', 0, // '0'
                1, 0, 0, 0, // 1
                16, // data_int
                '1', 0, // '1'
                2, 0, 0, 0, // 2
                16, // data_int
                '2', 0, // '2'
                3, 0, 0, 0, // 3
                16, // data_int
                '3', 0, // '3'
                4, 0, 0, 0, // 4
                16, // data_int
                '4', 0, // '4'
                5, 0, 0, 0, // 5
                16, // data_int
                '5', 0, // '5'
                6, 0, 0, 0, // 6
                0, // eoo array
                0, // eoo map bar
                0 // eoo bson
        };

        public BSONMap create() {
            return new BSONMap();
        }

        public void mapCanBeEncodedIntoBson() {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Map<String, Object> map = new HashMap<String, Object>() {
                {
                    put("foo", 1);
                    put("bar", new HashMap<String, Object>() {
                        {
                            put("zoo", 2);
                            put("brr", Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 6 }));
                        }
                    });
                }
            };
            context.write(new MapBSONCoders(), "foo", map, new LittleEndianDataWriter(out));
            specify(out.toByteArray(), does.containExactly(MAP_FIXTURE));
        }

        public void mapCanBeDecodedFromBson() {
            LittleEndianDataReader reader = new LittleEndianDataReader(new ByteArrayInputStream(MAP_FIXTURE));
            specify(reader.readByte(), does.equal((byte) 3));
            specify(context.readPath(reader, ""), does.equal("foo"));
            Map<String, Object> ret = context.read(new MapBSONCoders(), reader);
            specify(ret.get("foo"), does.equal(1));
            specify(ret.get("bar"), new HashMap<String, Object>() {
                {
                    put("zoo", 2);
                    put("brr", Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 6 }));
                }
            });
        }
    }

}
