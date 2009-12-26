package mungbean;

import java.util.Map;

public class Database {
	private final String dbName;
	private final DbOperationExecutor executor;

	public Database(DbOperationExecutor executor, String name) {
		this.dbName = name;
		this.executor = executor;
	}

	public DBCollection<Map<String, Object>> openCollection(String name) {
		return new MapDBCollection(executor, dbName, name);
	}

	public <T> DBCollection<T> openCollection(String name, Class<T> type) {
		return new PojoDBCollection<T>(openCollection(name), type);
	}

}
