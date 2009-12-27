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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

@RunWith(JDaveRunner.class)
public class MongoPerformanceTest extends Specification<DBCollection<Map<String, Object>>> {
	public class WithDatabase {
		Mungbean db;

		public DBCollection<Map<String, Object>> create() {
			db = new Mungbean("localhost", 27017);
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
