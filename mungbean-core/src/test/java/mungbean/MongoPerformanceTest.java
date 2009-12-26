package com.mongodb;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

@RunWith(JDaveRunner.class)
public class MongoPerformanceTest extends Specification<DBCollection> {
	public class WithDatabase {
		Mongo db;

		public DBCollection create() {
			db = new Mongo("localhost", 27017);
			return db.openDatabase("foobar").openCollection("foo");
		}

		public void destroy() {
			db.close();
		}

		public void databaseCanBeAccessed() throws InterruptedException {
			ExecutorService executor = Executors.newFixedThreadPool(10);
			StopWatch timer = new StopWatch();
			long total = 500000;
			for (int a = 0; a < total; a++) {
				executor.submit(new Runnable() {
					@Override
					public void run() {
						final ObjectId id = new ObjectId();
						context.insert(new HashMap<String, Object>() {
							{
								put("foo", "bar");
								put("_id", id);
							}
						});
						context.delete(id);
					}
				});
			}
			executor.shutdown();
			executor.awaitTermination(10, TimeUnit.SECONDS);
			long time = timer.millisecondsSinceStart();
			System.out.println("Insert+delete time for " + total + " items was " + time + "ms -> " + (total / (time / 1000)) + " operations per second.");
		}
	}
}
