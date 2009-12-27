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

import mungbean.protocol.DBConnection;

public class DbOperationExecutor extends Pool<DBConnection> {
	private final String host;
	private final int port;
	private boolean shuttingDown = false;

	public DbOperationExecutor(String host, int port) {
		this.host = host;
		this.port = port;
	}

	@Override
	public DBConnection createNew() {
		if (shuttingDown) {
			throw new IllegalStateException("Db connection is shut down.");
		}
		return new DBConnection(host, port);
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
			if (connection != null) {
				giveBack(connection);
			}
		}
	}

	public void close() {
		shuttingDown = true;
		DBConnection connection;
		while ((connection = poll()) != null) {
			connection.close();
		}
	}
}
