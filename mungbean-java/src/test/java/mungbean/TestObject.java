package mungbean;

public class TestObject {
	private final String name;
	private final Integer value;

	public TestObject(String name, Integer value) {
		this.name = name;
		this.value = value;
	}

	public String name() {
		return name;
	}

	public Integer value() {
		return value;
	}
}
