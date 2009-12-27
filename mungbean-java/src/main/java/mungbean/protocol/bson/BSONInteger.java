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

public class BSONInteger extends BSONCoder<Integer> {
	public BSONInteger() {
		super(16, Integer.class);
	}

	@Override
	protected Integer decode(AbstractBSONCoders bson, LittleEndianDataReader reader) {
		return reader.readInt();
	}

	@Override
	protected void encode(AbstractBSONCoders bson, Integer value, LittleEndianDataWriter writer) {
		writer.writeInt(value);
	}
}
