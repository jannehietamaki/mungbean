package mungbean.rhino;

import java.util.ArrayList;
import java.util.List;

import mungbean.protocol.bson.AbstractBSONMap;
import mungbean.protocol.bson.KeyValuePair;

import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;

public class RhinoBSONMap extends AbstractBSONMap<ScriptableObject> {
    public RhinoBSONMap() {
        super(ScriptableObject.class);
    }

    @Override
    protected ScriptableObject newInstance() {
        return new NativeObject();
    }

    @Override
    protected ScriptableObject setValue(ScriptableObject item, String key, Object value) {
        NativeObject.putProperty(item, key, value);
        return item;
    }

    @Override
    protected Iterable<KeyValuePair<String, Object>> entriesOf(ScriptableObject item) {
        List<KeyValuePair<String, Object>> returnValue = new ArrayList<KeyValuePair<String, Object>>();
        for (Object id : item.getIds()) {
            String key = String.valueOf(id);
            Object value = NativeObject.getProperty(item, key);
            returnValue.add(new KeyValuePair<String, Object>(key, value));
        }
        return returnValue;
    }
}
