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
import java.net.InetSocketAddress;
import java.net.Socket;

import mungbean.Authentication;
import mungbean.MongoException;
import mungbean.Server;
import mungbean.protocol.message.MongoRequest;
import mungbean.protocol.message.Response;

public class DBConnection {

    private static final int CONNECTION_TIMEOUT = 5000;
    private int requestIdCounter = 0;
    private final Socket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public DBConnection(Server server) {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(server.host(), server.port()), CONNECTION_TIMEOUT);
            inputStream = new BufferedInputStream(socket.getInputStream());
            outputStream = new BufferedOutputStream(socket.getOutputStream());
            for (Authentication authentication : server.authentication()) {
                authentication.authenticate(this);
            }
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    public DBConnection(InputStream input, OutputStream output) {
        this.inputStream = input;
        this.outputStream = output;
        this.socket = null;
    }

    public <T extends Response> T execute(MongoRequest<T> message) {
        DBTransaction<T> transaction = null;
        try {
            transaction = new DBTransaction<T>(message, incrementAndGetCounter());
            return transaction.call(outputStream, inputStream);
        } catch (Exception e) {
            // TODO We should probably close this connection as it's state might
            // be invalid
            throw new MongoException("Error communicating with server", e, transaction);
        }
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
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }
}
