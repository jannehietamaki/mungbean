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

import java.util.Map;

import mungbean.protocol.DBConnection;
import mungbean.protocol.RuntimeIOException;
import mungbean.protocol.message.CommandRequest;

public class SingleNodeDbOperationExecutor extends Pool<DBConnection> implements DBOperationExecutor {
	private boolean shuttingDown = false;
	private final Server server;

	public SingleNodeDbOperationExecutor(Server server) {
		this.server = server;
	}

	@Override
	protected DBConnection createNew() {
		if (shuttingDown) {
			throw new IllegalStateException("Db connection is shut down.");
		}
		return new DBConnection(server);
	}

	public <T> T execute(DBConversation<T> conversation) {
		if (shuttingDown) {
			throw new IllegalStateException("Db connection is shut down.");
		}
		DBConnection connection = null;
		try {
			connection = borrow();
			return conversation.execute(connection);
		} finally {
			giveBack(connection);
		}
	}

	@Override
	public <T> T executeWrite(DBConversation<T> conversation) {
		return execute(conversation);
	}

	public void close() {
		shuttingDown = true;
		DBConnection connection;
		while ((connection = poll()) != null) {
			connection.close();
		}
	}

	public boolean isAlive() {
		try {
			return execute(new DBConversation<Boolean>() {
				@Override
				public Boolean execute(DBConnection connection) {
					return doIsAlive(connection);
				}
			});
		} catch (RuntimeIOException e) {
			return false;
		}
	}

	private boolean doIsAlive(DBConnection connection) {
		connection.execute(new CommandRequest("ismaster"));
		return true;
	}

	public boolean isMaster() {
		try {
			return execute(new DBConversation<Boolean>() {
				@Override
				public Boolean execute(DBConnection connection) {
					Map<String, Object> response = connection.execute(new CommandRequest("$cmd", "ismaster"));
					return ((Number) response.get("ismaster")).intValue() == 1;
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
	protected boolean isValid(DBConnection item) {
		return doIsAlive(item);
	}
}
