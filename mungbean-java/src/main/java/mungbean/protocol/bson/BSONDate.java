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

import java.util.Date;

import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;

public class BSONDate extends BSONCoder<Date> {

	protected BSONDate() {
		super(9, Date.class);
	}

	@Override
	protected Date decode(BSONCoders bson, LittleEndianDataReader reader) {
		return new Date(reader.readLong());
	}

	@Override
	protected void encode(BSONCoders bson, Date date, LittleEndianDataWriter writer) {
		writer.writeLong(date.getTime());
	}
}
