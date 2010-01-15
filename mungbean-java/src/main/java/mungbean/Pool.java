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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Pool<T> {
    private final BlockingQueue<T> allObjects;
    private final BlockingQueue<T> availableObjects;
    private int maxOpenConnections;

    public Pool(int maxOpenConnections) {
        this.maxOpenConnections = maxOpenConnections;
        allObjects = new LinkedBlockingQueue<T>(maxOpenConnections);
        availableObjects = new LinkedBlockingQueue<T>();
    }

    protected abstract T createNew();

    protected abstract void close(T object);

    protected abstract boolean isValid(T item);

    public void giveBack(T object) {
        try {
            if (object != null && isValid(object)) {
                availableObjects.add(object);
                return;
            }
        } catch (RuntimeException e) {
            // isValid threw an exception - object is no longer valid and
            // can be thrown away
            allObjects.remove(object);
            try {
                close(object);
            } catch (Exception ex) {
            }
            throw e;
        }
    }

    public T borrow() {
        return getNext(true);
    }

    protected T getNext(boolean createNewIfRequired) {
        T item;
        do {
            item = availableObjects.poll();
            if (item == null) {
                if (allObjects.size() >= maxOpenConnections) {
                    try {
                        if (createNewIfRequired) {
                            item = availableObjects.take();
                        } else {
                            return availableObjects.poll();
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException("Received an interrput while waiting for available connection", e);
                    }
                } else {
                    if (createNewIfRequired) {
                        item = newItem();
                    } else {
                        return null;
                    }
                }
            }
        } while (!isValid(item));
        return item;
    }

    protected T newItem() {
        T item = createNew();
        allObjects.offer(item);
        return item;
    }
}