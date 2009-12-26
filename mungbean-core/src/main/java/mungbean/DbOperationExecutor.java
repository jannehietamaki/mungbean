package com.mongodb;

import com.mongodb.protocol.DBConnection;

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
