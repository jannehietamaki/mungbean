package mungbean.rhino;

import java.util.Date;
import mungbean.rhino.NativeArrayWrapper;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class RhinoWrapper {
    public static Object wrapRhinoToJava(Object o) {
        if (o instanceof NativeArray) {
            return new NativeArrayWrapper((NativeArray) o);
        } else if (NativeJavaObject.canConvert(o, Date.class)) {
            return ((java.util.Date) Context.jsToJava(o, Date.class));
        } else if (o instanceof ScriptableObject) {
            return new ScriptableObjectWrapper((ScriptableObject) o);
        }
        return o;
    }

    public static Object wrapJavaToRhino(Scriptable scriptable, Object o) {
        if (o instanceof Date) {
            Object[] args = { new Long(((Date) o).getTime()) };
            return Context.getCurrentContext().newObject(scriptable, "Date", args);
        }
        return o;
    }
}
