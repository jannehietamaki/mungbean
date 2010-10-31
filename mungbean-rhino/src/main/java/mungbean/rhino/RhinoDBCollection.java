package mungbean.rhino;

import mungbean.AbstractDBCollection;
import mungbean.DBOperationExecutor;
import mungbean.ObjectId;
import mungbean.protocol.bson.BSONCoder;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class RhinoDBCollection extends AbstractDBCollection<ScriptableObject> {

    public RhinoDBCollection(DBOperationExecutor executor, String dbName, String collectionName, Scriptable scriptable, Context context) {
        super(executor, dbName, collectionName, new RhinoBSONCoders(context, scriptable), new RhinoBSONCoders(context, scriptable));
    }

    @Override
    protected BSONCoder<ScriptableObject> defaultEncoder() {
        return new RhinoBSONMap();
    }

    @Override
    protected ScriptableObject injectId(ScriptableObject doc) {
        if (!NativeObject.hasProperty(doc, "_id")) {
            NativeObject.putProperty(doc, "_id", new ObjectId());
        }
        return doc;
    }
}
