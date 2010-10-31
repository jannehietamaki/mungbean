package mungbean.rhino;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import mungbean.AbstractDatabase;
import mungbean.DBOperationExecutor;

public class RhinoDatabase extends AbstractDatabase {

    public RhinoDatabase(DBOperationExecutor executor, String name) {
        super(executor, name);
    }

    public RhinoDBCollection openCollection(Scriptable scriptable, Context context, String name) {
        return new RhinoDBCollection(executor(), dbName(), name, scriptable, context);
    }
}
