package com.mongodb.protocol;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.mongodb.protocol.message.MongoRequest;

public class DBConnection {
	private final Socket socket;
	private final InputStream inputStream;
	private final OutputStream outputStream;

	public DBConnection(String server, int port) {
		try {
			socket = new Socket(server, port);
			inputStream = new BufferedInputStream(socket.getInputStream());
			outputStream = new BufferedOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}

	public <T> T execute(MongoRequest<T> message) {
		DBTransaction<T> transaction = new DBTransaction<T>(message, 0);
		transaction.send(outputStream);
		return transaction.readResponse(inputStream);
	}

	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}
}
