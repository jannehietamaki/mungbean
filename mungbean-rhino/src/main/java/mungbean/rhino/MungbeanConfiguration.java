package mungbean.rhino;

import java.util.ArrayList;
import java.util.List;

import mungbean.Authentication;
import mungbean.DBOperationExecutor;
import mungbean.Server;
import mungbean.Settings;
import mungbean.SingleNodeDbOperationExecutor;

public class MungbeanConfiguration {
    private final DBOperationExecutor executor;

    public MungbeanConfiguration(String host, int port) {
        List<Authentication> auths = new ArrayList<Authentication>();
        Settings settings = new Settings();
        executor = new SingleNodeDbOperationExecutor(settings, new Server(host, port, auths.toArray(new Authentication[auths.size()])));
    }

    public DBOperationExecutor executor() {
        return executor;
    }
}
