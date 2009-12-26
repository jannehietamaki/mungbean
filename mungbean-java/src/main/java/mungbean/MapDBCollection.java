package mungbean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mungbean.protocol.DBConnection;
import mungbean.protocol.message.DeleteRequest;
import mungbean.protocol.message.InsertRequest;
import mungbean.protocol.message.QueryOptionsBuilder;
import mungbean.protocol.message.QueryRequest;
import mungbean.protocol.message.QueryResponse;
import mungbean.protocol.message.UpdateOptionsBuilder;
import mungbean.protocol.message.UpdateRequest;

public class MapDBCollection implements DBCollection<Map<String, Object>> {
	private final DbOperationExecutor executor;
	private final String dbName;
	private final String collectionName;

	public MapDBCollection(DbOperationExecutor executor, String dbName, String collectionName) {
		this.executor = executor;
		this.dbName = dbName;
		this.collectionName = collectionName;
	}

	public void insert(final Map<String, Object> doc) {
		executor.execute(new DBConversation<Void>() {
			@SuppressWarnings("unchecked")
			@Override
			public Void execute(DBConnection connection) {
				connection.execute(new InsertRequest(dbName(), doc));
				return null;
			};
		});
	}

	public void delete(final Map<String, Object> doc) {
		executor.execute(new DBConversation<Void>() {
			@Override
			public Void execute(DBConnection connection) {
				connection.execute(new DeleteRequest(dbName(), doc));
				return null;
			};
		});
	}

	public void update(final Map<String, Object> query, final Map<String, Object> doc, boolean upsert) {
		final UpdateOptionsBuilder options = new UpdateOptionsBuilder();
		if (upsert) {
			options.upsert();
		}
		executor.execute(new DBConversation<Void>() {
			@Override
			public Void execute(DBConnection connection) {
				connection.execute(new UpdateRequest(dbName(), options, query, doc));
				return null;
			};
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mungbean.DBCollection#query(java.util.Map, int, int)
	 */
	public List<Map<String, Object>> query(final Map<String, Object> rules, final int first, final int items) {
		final QueryOptionsBuilder options = new QueryOptionsBuilder();
		return executor.execute(new DBConversation<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> execute(DBConnection connection) {
				QueryResponse response = connection.execute(new QueryRequest(dbName(), options, first, items, rules));
				return response.values();
			};
		});
	}

	String dbName() {
		return dbName + "." + collectionName;
	}

	public Map<String, Object> find(final ObjectId id) {
		List<Map<String, Object>> results = query(idQuery(id), 0, 1);
		if (results.isEmpty()) {
			throw new NotFoundException("Item with id " + id + " was not found");
		}
		return results.get(0);
	}

	private HashMap<String, Object> idQuery(final ObjectId id) {
		return new HashMap<String, Object>() {
			{
				put("_id", id);
			}
		};
	}

	public void delete(ObjectId id) {
		delete(idQuery(id));
	}
}
