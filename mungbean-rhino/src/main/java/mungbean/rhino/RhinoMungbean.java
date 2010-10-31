package mungbean.rhino;

import mungbean.DBOperationExecutor;

public class RhinoMungbean {
    private final DBOperationExecutor executor;

    public RhinoMungbean(DBOperationExecutor executor) {
        this.executor = executor;
    }

    public RhinoDatabase openDatabase(String name) {
        return new RhinoDatabase(executor, name);
    }

    public void close() {
        executor.close();
    }
}
