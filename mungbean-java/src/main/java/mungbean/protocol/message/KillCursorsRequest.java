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

import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;

public class KillCursorsRequest extends MongoRequest<NoResponseExpected> {
	private final long[] cursorIds;

	public KillCursorsRequest(long... cursorIds) {
		this.cursorIds = cursorIds;
	}

	@Override
	public int length() {
		return 4 + 4 + cursorIds.length * 8;
	}

	@Override
	public NoResponseExpected readResponse(LittleEndianDataReader reader) {
		return new NoResponseExpected();
	}

	@Override
	public void send(LittleEndianDataWriter writer) {
		writer.writeInt(0); // RESERVED
		writer.writeInt(cursorIds.length);
		for (long cursorId : cursorIds) {
			writer.writeLong(cursorId);
		}
	}

	@Override
	public RequestOpCode type() {
		return RequestOpCode.OP_KILL_CURSORS;
	}
}
