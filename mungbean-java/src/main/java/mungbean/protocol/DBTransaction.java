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
package mungbean.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import mungbean.protocol.message.MongoRequest;

public class DBTransaction<T> {
	private final MongoRequest<T> message;
	private final int requestId;
	private final int responseTo;

	public DBTransaction(MongoRequest<T> message, int requestId, int responseTo) {
		this.message = message;
		this.requestId = requestId;
		this.responseTo = responseTo;
	}

	public void send(OutputStream output) {
		LittleEndianDataWriter writer = new LittleEndianDataWriter(output);
		writer.writeInt(message.length() + 4 * 4);
		writer.writeInt(requestId);
		writer.writeInt(responseTo); // ResponseTo
		writer.writeInt(message.type().opCode());
		message.send(writer);
		try {
			output.flush();
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}

	public T readResponse(InputStream inputStream) {
		return message.readResponse(new LittleEndianDataReader(inputStream));
	}

}
