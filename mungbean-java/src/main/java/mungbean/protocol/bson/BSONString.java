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

import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;

public class BSONString extends BSONCoder<String> {

	public BSONString() {
		super(2, String.class);
	}

	@Override
	protected String decode(AbstractBSONCoders bson, LittleEndianDataReader reader) {
		reader.readInt(); // Skip length
		return reader.readCString();
	}

	@Override
	protected void encode(AbstractBSONCoders bson, String value, LittleEndianDataWriter writer) {
		writer.writeCStringWithLength(value);
	}

}
