package mungbean.rhino;

import java.util.Map;

import org.mozilla.javascript.ScriptableObject;

public class MapWrapper extends ScriptableObject {
    private final Map<String, Object> map;

    public MapWrapper(Map<String, Object> map) {
        this.map = map;
    }

    @Override
    public String getClassName() {
        return "Array";
    }

    public void put(String name, org.mozilla.javascript.Scriptable start, Object value) {
        map.put(name, value);
    }

    public Object get(String name, org.mozilla.javascript.Scriptable start) {
        return map.get(name);
    }

    public boolean has(String name, org.mozilla.javascript.Scriptable start) {
        return map.containsKey(name);
    }

    public Object[] getIds() {
        return map.keySet().toArray();
    }
}
