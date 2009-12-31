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
package mungbean.protocol.message;

import java.io.ByteArrayOutputStream;

import sun.misc.HexDumpEncoder;
import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;

public abstract class MongoRequest<ResponseType> {

	public abstract void send(LittleEndianDataWriter writer);

	public abstract ResponseType readResponse(LittleEndianDataReader reader);

	public abstract RequestOpCode type();

	public abstract int length();

	public String debugInfo() {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		send(new LittleEndianDataWriter(output));
		return "[MongoRequest: opCode= " + type() + " data:\n" + new HexDumpEncoder().encode(output.toByteArray()) + "]";
	}
}
