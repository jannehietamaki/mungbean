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
import java.util.concurrent.atomic.AtomicInteger;

import mungbean.DBConversation;
import mungbean.DBOperationExecutor;
import mungbean.MongoException;
import mungbean.Server;
import mungbean.Settings;
import mungbean.SingleNodeDbOperationExecutor;

public class RoundRobinResolverStrategy implements ServerResolverStrategy {
    private final AtomicInteger roundRobinCount = new AtomicInteger(0);

    private final List<SingleNodeDbOperationExecutor> allServers;

    public RoundRobinResolverStrategy(Settings settings, Server[] servers) {
        allServers = new ArrayList<SingleNodeDbOperationExecutor>();
        for (Server server : servers) {
            allServers.add(new SingleNodeDbOperationExecutor(settings, server));
        }
    }

    @Override
    public void close() {
        for (DBOperationExecutor server : allServers) {
            server.close();
        }
    }

    @Override
    public <T> T execute(DBConversation<T> conversation) {
        return getReadTarget().execute(conversation);
    }

    private SingleNodeDbOperationExecutor getReadTarget() {
        int numberOfServersAvailable = allServers.size();
        for (int a = 0; a < numberOfServersAvailable; a++) {
            SingleNodeDbOperationExecutor server = allServers.get(roundRobinCount.incrementAndGet() % numberOfServersAvailable);
            if (server.isAlive()) {
                return server;
            }
        }
        throw new MongoException("No servers available for read!");
    }

}
