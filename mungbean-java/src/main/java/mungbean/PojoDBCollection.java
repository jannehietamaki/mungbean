package mungbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import mungbean.util.FieldDefinition;
import mungbean.util.ReflectionUtil;

public class PojoDBCollection<T> implements DBCollection<T> {
	private final static Objenesis objenesis = new ObjenesisStd();

	private final DBCollection<Map<String, Object>> storage;
	private final Class<T> typeClass;

	public PojoDBCollection(DBCollection<Map<String, Object>> storage, Class<T> typeClass) {
		this.storage = storage;
		this.typeClass = typeClass;
	}

	@Override
	public void delete(Map<String, Object> query) {
		storage.delete(query);
	}

	@Override
	public void delete(ObjectId id) {
		storage.delete(id);
	}

	@Override
	public T find(ObjectId id) {
		return fromMap(storage.find(id));
	}

	@Override
	public void insert(T doc) {
		storage.insert(toMap(doc));
	}

	@Override
	public List<T> query(Map<String, Object> rules, int first, int items) {
		List<T> result = new ArrayList<T>();
		List<Map<String, Object>> docs = storage.query(rules, first, items);
		for (Map<String, Object> doc : docs) {
			result.add(fromMap(doc));
		}
		return result;
	}

	@Override
	public void update(Map<String, Object> query, T doc, boolean upsert) {
		storage.update(query, toMap(doc), upsert);
	}

	@SuppressWarnings("unchecked")
	private T fromMap(Map<String, Object> doc) {
		T ret = (T) objenesis.newInstance(typeClass);
		FieldDefinition[] fields = ReflectionUtil.fieldsOf(typeClass);
		for (FieldDefinition field : fields) {
			field.set(ret, doc.get(field.name()));
		}
		return ret;
	}

	private Map<String, Object> toMap(T doc) {
		Map<String, Object> ret = new HashMap<String, Object>();
		FieldDefinition[] fields = ReflectionUtil.fieldsOf(typeClass);
		for (FieldDefinition field : fields) {
			ret.put(field.name(), field.get(doc));
		}
		return ret;
	}

}
