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

import java.util.List;
import java.util.Map;

import mungbean.protocol.Connection;
import mungbean.protocol.bson.AbstractBSONCoders;
import mungbean.protocol.bson.BSONCoder;
import mungbean.protocol.command.AbstractCommand;
import mungbean.protocol.command.Aggregation;
import mungbean.protocol.command.AggregationCommand;
import mungbean.protocol.command.LastError;
import mungbean.protocol.message.CommandRequest;
import mungbean.protocol.message.CommandResponse;
import mungbean.protocol.message.DeleteRequest;
import mungbean.protocol.message.GetMoreRequest;
import mungbean.protocol.message.InsertRequest;
import mungbean.protocol.message.KillCursorsRequest;
import mungbean.protocol.message.QueryOptionsBuilder;
import mungbean.protocol.message.QueryRequest;
import mungbean.protocol.message.QueryResponse;
import mungbean.protocol.message.UpdateOptionsBuilder;
import mungbean.protocol.message.UpdateRequest;
import mungbean.query.Query;
import mungbean.query.QueryBuilder;
import mungbean.query.UpdateBuilder;

public abstract class AbstractDBCollection<T> implements DBCollection<T> {

    private final DBOperationExecutor executor;
    private final String dbName;
    private final String collectionName;
    private final AbstractBSONCoders documentCoders;
    private final AbstractBSONCoders queryCoders;

    public AbstractDBCollection(DBOperationExecutor executor, String dbName, String collectionName, AbstractBSONCoders documentCoders, AbstractBSONCoders queryCoders) {
        this.executor = executor;
        this.dbName = dbName;
        this.collectionName = collectionName;
        this.documentCoders = documentCoders;
        this.queryCoders = queryCoders;
    }

    public CollectionAdmin collectionAdmin() {
        return new CollectionAdmin(this);
    }

    @Override
    public String collectionName() {
        return collectionName;
    }

    protected abstract BSONCoder<T> defaultEncoder();

    public T save(final T doc) {
        final T newDoc = injectId(doc);
        return executeWrite(new ErrorCheckingDBConversation() {
            @SuppressWarnings("unchecked")
            @Override
            public T doExecuteWithErrorChecking(Connection connection) {
                connection.execute(new InsertRequest<T>(dbName(), documentCoders, newDoc));
                return newDoc;
            }
        });

    }

    public void save(final T... docs) {
        for (T doc : docs) {
            injectId(doc);
        }
        executeWrite(new ErrorCheckingDBConversation() {
            @Override
            public T doExecuteWithErrorChecking(Connection connection) {
                // TODO if all docs do not fit into one request -> split into
                // multiple batches
                connection.execute(new InsertRequest<T>(dbName(), documentCoders, docs));
                return null;
            }
        });
    }

    protected abstract T injectId(T doc);

    public void delete(final QueryBuilder query) {
        executeWrite(new ErrorCheckingDBConversation() {
            @Override
            public T doExecuteWithErrorChecking(Connection connection) {
                connection.execute(new DeleteRequest(dbName(), queryCoders, query));
                return null;
            };
        });
    }

    public void update(final ObjectId id, final T doc) {
        executeWrite(new ErrorCheckingDBConversation() {
            @Override
            public T doExecuteWithErrorChecking(Connection connection) {
                connection.execute(new UpdateRequest<T>(dbName(), idQuery(id), new UpdateOptionsBuilder(), doc, documentCoders, queryCoders));
                return null;
            };
        });
    }

    public void update(final QueryBuilder query, final UpdateBuilder update) {
        executeWrite(new ErrorCheckingDBConversation() {
            @Override
            public T doExecuteWithErrorChecking(Connection connection) {
                connection.execute(new UpdateRequest<T>(dbName(), query, update, documentCoders, queryCoders));
                return null;
            };
        });
    }

    public List<T> query(final QueryBuilder query) {
        ListQueryCallback<T> callback = new ListQueryCallback<T>();
        query(query, callback);
        return callback.values();
    }

    public void query(final QueryBuilder query, final QueryCallback<T> callback) {
        execute(new DBConversation<Void>() {
            @Override
            public Void execute(Connection connection) {
                QueryResponse<T> response = connection.execute(new QueryRequest<T>(dbName(), new QueryOptionsBuilder(), query, queryCoders, defaultEncoder()));
                // TODO Check response.responseFlag for errors
                boolean readMore = response.readResponse(callback);
                long cursorId = response.cursorId();
                try {
                    if (cursorId != 0 && readMore) {
                        QueryResponse<T> queryResponse;
                        do {
                            // TODO calculate the number of items
                            queryResponse = connection.execute(new GetMoreRequest<T>(dbName(), cursorId, 0, defaultEncoder(), documentCoders));
                            readMore = queryResponse.readResponse(callback);
                            cursorId = queryResponse.cursorId();
                        } while (readMore && cursorId != 0);
                    }
                    return null;
                } finally {
                    if (cursorId != 0) {
                        connection.execute(new KillCursorsRequest(cursorId));
                    }
                }
            };
        });
    }

    public <ReturnType> ReturnType query(Aggregation<ReturnType> aggregation, QueryBuilder query) {
        return command(new AggregationCommand<ReturnType>(aggregation, query));
    }

    String dbName() {
        return dbName + "." + collectionName;
    }

    public T find(final ObjectId id) {
        List<T> results = query(idQuery(id));
        if (results.isEmpty()) {
            throw new NotFoundException("Item with id " + id + " was not found");
        }
        return results.get(0);
    }

    private QueryBuilder idQuery(final ObjectId id) {
        return new Query().setLimit(1).field("_id").is(id);
    }

    public void delete(ObjectId id) {
        delete(idQuery(id));
    }

    @Override
    public <ResponseType> ResponseType command(final AbstractCommand<ResponseType> command) {
        return executeWrite(new DBConversation<ResponseType>() {
            @Override
            public ResponseType execute(Connection connection) {
                return executeCommand(command, connection);
            }
        });
    }

    private <ResponseType> ResponseType executeCommand(AbstractCommand<ResponseType> command, Connection connection) {
        CommandResponse response = connection.execute(new CommandRequest(dbName, command.requestMap(AbstractDBCollection.this), queryCoders));
        if (response == null) {
            throw new NotFoundException("Value not returned for command: " + command);
        }
        Object result = response.get("ok");
        if (!result.equals(1D) && !result.equals(Boolean.TRUE)) {
            throw new MongoException("Command execution failed", response);
        }
        return command.parseResponse(response);
    }

    private abstract class ErrorCheckingDBConversation extends DBConversation<T> {
        @Override
        public final T execute(Connection connection) {
            T value = doExecuteWithErrorChecking(connection);
            Map<String, Object> message = executeCommand(new LastError(), connection);
            if (message.get("err") != null) {
                throw MongoException.forResponse(message);
            }
            return value;
        }

        protected abstract T doExecuteWithErrorChecking(Connection connection);
    }

    public <V> V execute(DBConversation<V> conversation) {
        return executor.execute(conversation);
    }

    public <V> V executeWrite(DBConversation<V> conversation) {
        return executor.executeWrite(conversation);
    }
}
