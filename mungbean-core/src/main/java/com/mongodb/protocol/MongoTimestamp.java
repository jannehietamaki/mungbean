package com.mongodb.protocol;

public class MongoTimestamp {

	private final int incrementedField;
	private final int time;

	public MongoTimestamp() {
		incrementedField = 0;
		time = 0;
	}

	public MongoTimestamp(int time, int incrementedField) {
		this.time = time;
		this.incrementedField = incrementedField;
	}

	public int time() {
		return time;
	}

	public int incrementedField() {
		return incrementedField;
	}
}
