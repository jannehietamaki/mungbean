package mungbean.rhino;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import mungbean.protocol.bson.AbstractBSONArray;
import mungbean.protocol.bson.KeyValuePair;

public class RhinoBSONList<T> extends AbstractBSONArray<NativeArray, T> {
    private final Scriptable scriptable;

    public RhinoBSONList(Scriptable scriptable) {
        super(NativeArray.class);
        this.scriptable = scriptable;
    }

    @Override
    protected NativeArray addValue(NativeArray ret, T value) {
        NativeArray newArray = new NativeArray(ret.getLength() + 1);
        for (int a = 0; a < ret.getLength(); a++) {
            Object value1 = ScriptableObject.getProperty(ret, a);
            ScriptableObject.putProperty(newArray, a, value1);
        }
        ScriptableObject.putProperty(newArray, (int) ret.getLength(), value);
        return newArray;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Iterable<KeyValuePair<Integer, T>> valuesOf(NativeArray l) {
        List<KeyValuePair<Integer, T>> returnValues = new ArrayList<KeyValuePair<Integer, T>>();
        for (int i = 0; i < l.getLength(); i++) {
            T value = (T) l.get(i, scriptable);
            returnValues.add(new KeyValuePair<Integer, T>(i, value));
        }
        return returnValues;
    }

    @Override
    protected NativeArray newInstance() {
        return new NativeArray(0);
    }
}