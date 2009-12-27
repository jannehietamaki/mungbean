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

import mungbean.ObjectId;
import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;

public class BSONOid extends BSONCoder<ObjectId> {

	protected BSONOid() {
		super(7, ObjectId.class);
	}

	@Override
	protected ObjectId decode(BSONCoders bson, LittleEndianDataReader reader) {
		byte[] bytes = new byte[12];
		reader.read(bytes);
		return new ObjectId(bytes);
	}

	@Override
	protected void encode(BSONCoders bson, ObjectId oid, LittleEndianDataWriter writer) {
		writer.write(oid.toByteArray());
	}
}
