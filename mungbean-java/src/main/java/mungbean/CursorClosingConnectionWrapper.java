/*
   Copyright 2009-2010 Janne Hietamäki

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

package mungbean;

import java.util.HashSet;
import java.util.Set;

import mungbean.protocol.Connection;
import mungbean.protocol.message.KillCursorsRequest;
import mungbean.protocol.message.MongoRequest;
import mungbean.protocol.message.Response;

public class CursorClosingConnectionWrapper implements Connection {
    private final Set<Long> openCursors = new HashSet<Long>();
    private final Connection connection;

    public CursorClosingConnectionWrapper(Connection connection) {
        this.connection = connection;
    }

    public void closeCursors() {
        if (!openCursors.isEmpty()) {
            connection.execute(new KillCursorsRequest(openCursors.toArray(new Long[openCursors.size()])));
        }
    }

    @Override
    public <T extends Response> T execute(MongoRequest<T> message) {
        T response = connection.execute(message);
        recordCursorId(response);
        return response;
    }

    private <T extends Response> void recordCursorId(T response) {
        long cursorId = response.cursorId();
        if (cursorId > 0) {
            openCursors.add(response.cursorId());
        }
    }

    @Override
    public void close() {
        connection.close();
    }

}
