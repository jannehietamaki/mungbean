package mungbean;

import mungbean.protocol.DBConnection;

public interface DBConversation<T> {
	public T execute(DBConnection connection);
}
