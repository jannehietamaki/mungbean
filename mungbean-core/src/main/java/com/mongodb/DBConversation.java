package com.mongodb;

import com.mongodb.protocol.DBConnection;

public interface DBConversation<T> {
	public T execute(DBConnection connection);
}
