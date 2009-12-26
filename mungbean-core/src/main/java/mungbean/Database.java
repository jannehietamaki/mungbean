package com.mongodb;

public class Database {
	private final String dbName;
	private final DbOperationExecutor executor;

	public Database(DbOperationExecutor executor, String name) {
		this.dbName = name;
		this.executor = executor;
	}

	public DBCollection openCollection(String name) {
		return new DBCollection(executor, dbName, name);
	}

}
