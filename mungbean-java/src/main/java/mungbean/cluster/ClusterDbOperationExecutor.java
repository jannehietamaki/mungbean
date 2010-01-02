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

import mungbean.DBConversation;
import mungbean.DBOperationExecutor;
import mungbean.Server;

public class ClusterDbOperationExecutor implements DBOperationExecutor {
    // TODO need a mechanism to specify if it's ok to run the query against
    // slave/passive-master
    private final ServerResolverStrategy writeResolver;
    private final ServerResolverStrategy readResolver;

    public ClusterDbOperationExecutor(Server... servers) {
        writeResolver = new MasterResolverStrategy(servers);
        readResolver = writeResolver;
    }

    @Override
    public void close() {
        writeResolver.close();
        readResolver.close();
    }

    @Override
    public <T> T executeWrite(DBConversation<T> conversation) {
        return writeResolver.execute(conversation);
    }

    @Override
    public <T> T execute(DBConversation<T> conversation) {
        return readResolver.execute(conversation);
    }

}
