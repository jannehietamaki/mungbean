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

package mungbean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ClusterDbOperationExecutor implements DBOperationExecutor {
	// TODO need a mechanism to specify if it's ok to run the query against
	// slave/passive-master

	private final AtomicReference<SingleNodeDbOperationExecutor> currentMaster = new AtomicReference<SingleNodeDbOperationExecutor>();
	private final List<SingleNodeDbOperationExecutor> allServers;
	private final AtomicInteger roundRobinCount = new AtomicInteger(0);

	public ClusterDbOperationExecutor(Server... servers) {
		allServers = new ArrayList<SingleNodeDbOperationExecutor>();
		for (Server server : servers) {
			allServers.add(new SingleNodeDbOperationExecutor(server));
		}
		findNewMaster();
	}

	@Override
	public void close() {
		for (DBOperationExecutor server : allServers) {
			server.close();
		}
	}

	@Override
	public <T> T executeWrite(DBConversation<T> conversation) {
		try {
			return getWriteTarget().execute(conversation);
		} catch (Exception e) {
			return findNewMaster().execute(conversation);
		}
	}

	@Override
	public <T> T execute(DBConversation<T> conversation) {
		return getReadTarget().execute(conversation);
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
			System.out.println(server.server().port() + " -> alive =" + server.isAlive() + " master=" + server.isMaster());
			if (server.isMaster() && server.isAlive()) {
				currentMaster.set(server);
				return server;
			}
		}
		throw new MongoException("Unable to find master!");
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
