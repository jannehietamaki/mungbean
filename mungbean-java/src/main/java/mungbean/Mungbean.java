package mungbean;

public class Mungbean {
	private final DbOperationExecutor executor;

	public Mungbean(String host, int port) {
		executor = new DbOperationExecutor(host, port);
	}

	public Database openDatabase(String name) {
		return new Database(executor, name);
	}

	public void close() {
		executor.close();
	}
}
