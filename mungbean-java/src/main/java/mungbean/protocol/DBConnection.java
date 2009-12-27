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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import mungbean.protocol.message.MongoRequest;

public class DBConnection {
	private int requestIdCounter = 0;
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
		DBTransaction<T> transaction = new DBTransaction<T>(message, incrementAndGetCounter(), -1);
		transaction.send(outputStream);
		return transaction.readResponse(inputStream);
	}

	private int incrementAndGetCounter() {
		requestIdCounter++;
		if (requestIdCounter == Integer.MAX_VALUE) {
			requestIdCounter = 1;
		}
		return requestIdCounter;
	}

	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}
}
