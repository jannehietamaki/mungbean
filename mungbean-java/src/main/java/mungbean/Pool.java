package mungbean;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class Pool<T> {
	private final Queue<T> objects;

	public Pool() {
		objects = new ConcurrentLinkedQueue<T>();
	}

	public Pool(Collection<? extends T> objects) {
		this.objects = new ConcurrentLinkedQueue<T>(objects);
	}

	public abstract T createNew();

	public T borrow() {
		T t;
		if ((t = poll()) == null) {
			t = createNew();
		}
		return t;
	}

	public void giveBack(T object) {
		objects.offer(object);
	}

	protected T poll() {
		return objects.poll();
	}

}