package mungbean;

public class TestObjectWithId extends TestObject {
	private final ObjectId _id = new ObjectId();

	public TestObjectWithId(String name, Integer value) {
		super(name, value);
	}

	public ObjectId id() {
		return _id;
	}
}
