package mungbean;

import java.util.List;
import java.util.Map;

public interface DBCollection<T> {

	void insert(T doc);

	void delete(Map<String, Object> query);

	void update(Map<String, Object> query, T doc, boolean upsert);

	List<T> query(Map<String, Object> rules, int first, int items);

	T find(ObjectId id);

	void delete(ObjectId id);
}