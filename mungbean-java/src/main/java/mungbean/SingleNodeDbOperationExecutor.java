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
package mungbean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import mungbean.protocol.Connection;
import mungbean.protocol.DBConnection;
import mungbean.protocol.RuntimeIOException;
import mungbean.protocol.message.CommandRequest;
import mungbean.protocol.message.CommandResponse;

public class SingleNodeDbOperationExecutor extends Pool<Connection> implements DBOperationExecutor {
    private boolean shuttingDown = false;
    private final Server server;

    // TODO make these configurable
    private final static int maxOpenConnections = 10;
    private final static int initialConnections = 3;

    public SingleNodeDbOperationExecutor(Server server) {
        super(maxOpenConnections);
        this.server = server;
        Assert.isTrue(maxOpenConnections >= initialConnections, "Initial number of collections can not be more than maximum amount of connections");
        for (int a = 0; a < initialConnections; a++) {
            giveBack(newItem());
        }
    }

    @Override
    protected DBConnection createNew() {
        checkPoolStatus();
        return new DBConnection(server);
    }

    public <T> T execute(DBConversation<T> conversation) {
        checkPoolStatus();
        Connection connection = null;
        try {
            connection = borrow();
            return conversation.execute(connection);
        } finally {
            try {
                giveBack(connection);
            } catch (RuntimeException e) {
                // If the connection is no more valid we're getting an exception
                // here
                throw new RuntimeException("Connection state invalid, operation was " + generateRequestDebug(conversation), e);
            }
        }
    }

    @Override
    public <T> T executeWrite(DBConversation<T> conversation) {
        return execute(conversation);
    }

    public void close() {
        shuttingDown = true;
        Connection connection;
        while ((connection = getNext(false)) != null) {
            connection.close();
        }
    }

    public boolean isAlive() {
        try {
            return execute(new DBConversation<Boolean>() {
                @Override
                public Boolean doExecute(Connection connection) {
                    return isValid(connection);
                }
            });
        } catch (RuntimeIOException e) {
            return false;
        }
    }

    public boolean isMaster() {
        try {
            return execute(new DBConversation<Boolean>() {
                @Override
                public Boolean doExecute(Connection connection) {
                    CommandResponse response = connection.execute(new CommandRequest("$cmd", "ismaster"));
                    return response.getLong("ismaster") == 1;
                }
            });
        } catch (RuntimeIOException e) {
            return false;
        }
    }

    public Server server() {
        return server;
    }

    @Override
    protected boolean isValid(Connection connection) {
        connection.execute(new CommandRequest("ismaster"));
        return true;
    }

    private void checkPoolStatus() {
        if (shuttingDown) {
            throw new IllegalStateException("Db connection is shut down.");
        }
    }

    @Override
    protected void close(Connection conn) {
        conn.close();
    }

    private static String generateRequestDebug(DBConversation<?> conversation) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            conversation.execute(new DBConnection(new ByteArrayInputStream(new byte[0]), output));
        } catch (Exception e) {
        }
        return Utils.hexDump(output.toByteArray());
    }
}