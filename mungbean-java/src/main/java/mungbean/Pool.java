/*
   Copyright 2009 Janne Hietamäki

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package mungbean;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class Pool<T> {
	private final Queue<T> objects;

	public Pool() {
		objects = new ConcurrentLinkedQueue<T>();
	}

	protected abstract T createNew();

	protected abstract boolean isValid(T item);

	public Pool(Collection<? extends T> objects) {
		this.objects = new ConcurrentLinkedQueue<T>(objects);
	}

	public T borrow() {
		T t;
		if ((t = poll()) == null) {
			t = createNew();
		}
		return t;
	}

	public void giveBack(T object) {
		if (object != null && isValid(object)) {
			objects.offer(object);
		}
	}

	protected T poll() {
		T item;
		do {
			item = objects.poll();
		} while (item != null && !isValid(item));
		return item;
	}

}