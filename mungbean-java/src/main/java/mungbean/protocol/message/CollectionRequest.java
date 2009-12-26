package mungbean.protocol.message;

public abstract class CollectionRequest<ReturnType> extends MongoRequest<ReturnType> {
	private final String collectionName;

	public CollectionRequest(String collectionName) {
		this.collectionName = collectionName;
	}

	protected String collectionName() {
		return collectionName;
	}

	protected int collectionNameLength() {
		return 1 + collectionName.getBytes(UTF8).length;
	}
}
