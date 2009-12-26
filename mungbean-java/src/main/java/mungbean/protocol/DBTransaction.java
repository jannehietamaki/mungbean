package mungbean.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import mungbean.protocol.message.MongoRequest;

public class DBTransaction<T> {
	private final MongoRequest<T> message;
	private final int requestId;

	public DBTransaction(MongoRequest<T> message, int requestId) {
		this.message = message;
		this.requestId = requestId;
	}

	public void send(OutputStream output) {
		LittleEndianDataWriter writer = new LittleEndianDataWriter(output);
		writer.writeInt(message.length() + 4 * 4);
		writer.writeInt(requestId);
		writer.writeInt(0); // ResponseTo
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
