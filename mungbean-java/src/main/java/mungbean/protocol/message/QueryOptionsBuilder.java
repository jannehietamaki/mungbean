package mungbean.protocol.message;

public class QueryOptionsBuilder {
	private int value = 0;

	public QueryOptionsBuilder tailableCursor() {
		value = value | 2;
		return this;
	}

	public QueryOptionsBuilder slaveOk() {
		value = value | 4;
		return this;
	}

	public QueryOptionsBuilder oplogReplay() {
		value = value | 8;
		return this;
	}

	public QueryOptionsBuilder noCursorTimeout() {
		value = value | 16;
		return this;
	}

	public int build() {
		return value;
	}
}
