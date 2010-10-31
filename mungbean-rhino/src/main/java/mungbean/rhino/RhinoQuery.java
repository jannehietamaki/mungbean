package mungbean.rhino;

import java.util.HashMap;
import java.util.Map;

import mungbean.query.Query;

import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;

import mungbean.rhino.MapWrapper;
import mungbean.rhino.ScriptableObjectWrapper;

public class RhinoQuery extends Query {

    public RhinoQuery(ScriptableObject query, ScriptableObject options) {
        super(toMap(query), getProperty(options, "first", 0), getProperty(options, "items", 1000));
        if (options != null && NativeObject.hasProperty(options, "order")) {
            ScriptableObject order = (ScriptableObject) NativeObject.getProperty(options, "order");
            for (Object id : NativeObject.getPropertyIds(order)) {
                String key = String.valueOf(id);
                Number num = (Number) NativeObject.getProperty(order, key);
                if (num.intValue() > 0) {
                    this.field(key).orderAscending();
                } else {
                    this.field(key).orderDescending();
                }
            }
        }
    }

    static Map<String, Object> toMap(ScriptableObject query) {
        if (query == null) {
            query = new MapWrapper(new HashMap<String, Object>());
        }
        return new ScriptableObjectWrapper(query);
    }

    @SuppressWarnings("unchecked")
    static <T> T getProperty(ScriptableObject source, String key, T defaultValue) {
        if (source != null && NativeObject.hasProperty(source, key)) {
            return (T) NativeObject.getProperty(source, "order");
        }
        return defaultValue;
    }
}
