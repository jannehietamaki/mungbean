package mungbean.rhino;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mungbean.ObjectId;

import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import static mungbean.rhino.RhinoWrapper.wrapRhinoToJava;

public class ScriptableObjectWrapper implements Map<String, Object> {
    private final ScriptableObject n;

    public ScriptableObjectWrapper(ScriptableObject n) {
        this.n = n;
    }

    @Override
    public int size() {
        return n.getIds().length;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return NativeObject.hasProperty(n, String.valueOf(key));
    }

    @Override
    public boolean containsValue(Object value) {
        throw new NotImplementedException();
    }

    @Override
    public Object get(Object key) {
        String keyString = String.valueOf(key);
        Object o = NativeObject.getProperty(n, keyString);
        if (keyString.equals("_id") && o instanceof String) {
            return new ObjectId((String) o);
        }
        return wrapRhinoToJava(o);
    }

    @Override
    public Object put(String key, Object value) {
        throw new NotImplementedException();
    }

    @Override
    public Object remove(Object key) {
        throw new NotImplementedException();
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> m) {
        throw new NotImplementedException();
    }

    @Override
    public void clear() {
        throw new NotImplementedException();
    }

    @Override
    public Set<String> keySet() {
        List<Object> ids = Arrays.asList(n.getAllIds());
        Set<String> result = new LinkedHashSet<String>();
        for (Object o : ids) {
            result.add(String.valueOf(o));
        }
        return result;
    }

    @Override
    public Collection<Object> values() {
        throw new NotImplementedException();
    }

    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        Set<Entry<String, Object>> ret = new LinkedHashSet<Entry<String, Object>>();
        for (final String id : keySet()) {
            ret.add(new Map.Entry<String, Object>() {
                @Override
                public String getKey() {
                    return id;
                }

                @Override
                public Object getValue() {
                    return get(id);
                }

                @Override
                public Object setValue(Object value) {
                    return null;
                }
            });
        }
        return ret;
    }

    public String toString() {
        return "[Wrapper: " + n + "]";
    }
}
