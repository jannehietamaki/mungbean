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

public class MongoResponse {

	private final int messageLength;
	private final int requestId;
	private final int responseTo;
	private final RequestOpCode opCode;

	public MongoResponse(LittleEndianDataReader reader) {
		messageLength = reader.readInt();
		requestId = reader.readInt();
		// TODO Assert responseTo id
		responseTo = reader.readInt();
		opCode = RequestOpCode.forId(reader.readInt());
	}

	public int messageLength() {
		return messageLength;
	}

	public int requestId() {
		return requestId;
	}

	public int responseTo() {
		return responseTo;
	}

	public RequestOpCode opCode() {
		return opCode;
	}
}
