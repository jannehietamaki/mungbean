/*
   Copyright 2009 Janne Hietamäki

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package mungbean.cluster;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import mungbean.DBConversation;
import mungbean.DBOperationExecutor;
import mungbean.MongoException;
import mungbean.Server;
import mungbean.Settings;
import mungbean.SingleNodeDbOperationExecutor;

public class MasterResolverStrategy implements ServerResolverStrategy {
    private final AtomicReference<SingleNodeDbOperationExecutor> currentMaster = new AtomicReference<SingleNodeDbOperationExecutor>();
    private final List<SingleNodeDbOperationExecutor> allServers;

    public MasterResolverStrategy(Settings settings, Server[] servers) {
        allServers = new ArrayList<SingleNodeDbOperationExecutor>();
        for (Server server : servers) {
            allServers.add(new SingleNodeDbOperationExecutor(settings, server));
        }
        findNewMaster();
    }

    @Override
    public <T> T execute(DBConversation<T> conversation) {
        try {
            return getWriteTarget().execute(conversation);
        } catch (Exception e) {
            // TODO only try to find new master when error indicates this node is not a master
            return findNewMaster().execute(conversation);
        }
    }

    private SingleNodeDbOperationExecutor getWriteTarget() {
        SingleNodeDbOperationExecutor server = currentMaster.get();
        if (server.isAlive() && server.isMaster()) {
            return server;
        }
        return findNewMaster();
    }

    private SingleNodeDbOperationExecutor findNewMaster() {
        for (SingleNodeDbOperationExecutor server : allServers) {
            if (server.isMaster() && server.isAlive()) {
                currentMaster.set(server);
                return server;
            }
        }
        throw new MongoException("Unable to find master!");
    }

    @Override
    public void close() {
        for (DBOperationExecutor server : allServers) {
            server.close();
        }
    }
}
