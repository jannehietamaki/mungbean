package mungbean.rhino;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicInteger;

import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.UniqueTag;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import static mungbean.rhino.RhinoWrapper.wrapRhinoToJava;

public class NativeArrayWrapper implements List<Object> {
    private final NativeArray a;

    public NativeArrayWrapper(NativeArray a) {
        this.a = a;
    }

    @Override
    public int size() {
        return (int) a.getLength();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        throw new NotImplementedException();
    }

    @Override
    public Iterator<Object> iterator() {
        return listIterator();
    }

    @Override
    public Object[] toArray() {
        throw new NotImplementedException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new NotImplementedException();
    }

    @Override
    public boolean add(Object e) {
        throw new NotImplementedException();
    }

    @Override
    public boolean remove(Object o) {
        throw new NotImplementedException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new NotImplementedException();
    }

    @Override
    public boolean addAll(Collection<? extends Object> c) {
        throw new NotImplementedException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends Object> c) {
        throw new NotImplementedException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new NotImplementedException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new NotImplementedException();
    }

    @Override
    public void clear() {
        throw new NotImplementedException();
    }

    @Override
    public Object get(int index) {
        Object value = NativeArray.getProperty(a, index);
        if (value == UniqueTag.NOT_FOUND) {
            throw new IllegalArgumentException("Invalid index " + index);
        }
        return wrapRhinoToJava(value);
    }

    @Override
    public Object set(int index, Object element) {
        throw new NotImplementedException();
    }

    @Override
    public void add(int index, Object element) {
        throw new NotImplementedException();
    }

    @Override
    public Object remove(int index) {
        throw new NotImplementedException();
    }

    @Override
    public int indexOf(Object o) {
        throw new NotImplementedException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new NotImplementedException();
    }

    @Override
    public ListIterator<Object> listIterator() {
        final AtomicInteger counter = new AtomicInteger(0);
        return new ListIterator<Object>() {
            @Override
            public boolean hasNext() {
                return counter.get() < size();
            }

            @Override
            public Object next() {
                return get(counter.getAndIncrement());
            }

            @Override
            public boolean hasPrevious() {
                return counter.get() > 0;
            }

            @Override
            public Object previous() {
                return get(counter.decrementAndGet());
            }

            @Override
            public int nextIndex() {
                return counter.get() + 1;
            }

            @Override
            public int previousIndex() {
                return counter.get() - 1;
            }

            @Override
            public void remove() {

            }

            @Override
            public void set(Object e) {
            }

            @Override
            public void add(Object e) {
            }
        };
    }

    @Override
    public ListIterator<Object> listIterator(int index) {
        throw new NotImplementedException();
    }

    @Override
    public List<Object> subList(int fromIndex, int toIndex) {
        throw new NotImplementedException();
    }

    public String toString() {
        return "[Wrapper: " + a + "]";
    }
}