package com.mongodb.protocol.message;

public class UpdateOptionsBuilder {
	private int value;

	public UpdateOptionsBuilder upsert() {
		value = value | 1;
		return this;
	}

	public UpdateOptionsBuilder multiUpdate() {
		value = value | 2;
		return this;
	}

	public int value() {
		return value;
	}
}
