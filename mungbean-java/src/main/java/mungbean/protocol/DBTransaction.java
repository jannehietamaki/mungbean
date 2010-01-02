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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import sun.misc.HexDumpEncoder;

import mungbean.MongoException;
import mungbean.protocol.message.MongoRequest;
import mungbean.protocol.message.Response;

public class DBTransaction<T extends Response> {
    private final MongoRequest<T> message;
    private final int requestId;
    private final int responseTo = -1;

    public DBTransaction(MongoRequest<T> message, int requestId) {
        this.message = message;
        this.requestId = requestId;
    }

    public void sendRequest(OutputStream output) {
        LittleEndianDataWriter writer = new LittleEndianDataWriter(output);
        writer.writeInt(message.length() + 4 * 4);
        writer.writeInt(requestId);
        writer.writeInt(responseTo);
        writer.writeInt(message.type().opCode());
        message.send(writer);
        // TODO no need to flush until end of the transaction when operation
        // does not expect immediate response from the server
        try {
            output.flush();
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    public T readResponse(InputStream inputStream) {
        return message.readResponse(new LittleEndianDataReader(inputStream));
    }

    public T call(OutputStream outputStream, InputStream inputStream) {
        sendRequest(outputStream);
        T response = readResponse(inputStream);
        validateResponse(response);
        return response;
    }

    private void validateResponse(T response) {
        int responseId = response.responseTo();
        if (responseId != -1) {
            if (responseId != requestId) {
                throw new MongoException("Received response to unexpected request " + requestId + "!=" + responseId);
            }
        }
    }

    public String debugInfo() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        sendRequest(output);
        return "[" + getClass().getName() + ": opCode= " + message.type() + " data:\n" + new HexDumpEncoder().encode(output.toByteArray()) + "]";
    }
}
