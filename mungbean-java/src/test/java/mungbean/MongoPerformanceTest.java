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
import java.util.concurrent.atomic.AtomicInteger;

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import mungbean.protocol.command.Count;
import mungbean.query.Query;

import org.junit.runner.RunWith;

@RunWith(JDaveRunner.class)
public class MongoPerformanceTest extends Specification<Database> {
    private final static String contentString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam ac mi urna, sit amet iaculis ligula. Vestibulum malesuada tortor ut quam posuere nec tempor eros interdum. Nullam massa nulla, ultrices in ultricies id, pellentesque sed quam. Mauris at adipiscing elit. Maecenas facilisis justo et tortor tristique faucibus. Proin tincidunt lacinia nunc quis egestas. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Nulla orci quam, hendrerit non tempus eu, lacinia eu felis. Donec dictum adipiscing odio et aliquam. Proin nec porta ante. Fusce ac massa tellus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Curabitur pretium, ante ac porttitor aliquam, eros mauris porta tortor, non dictum dui leo ac tellus. Ut in lorem magna. Ut placerat convallis mi non pellentesque. Aliquam feugiat, mauris suscipit interdum consectetur, mauris arcu tincidunt lorem, condimentum adipiscing felis erat quis lorem. Curabitur dui nulla, elementum eu eleifend id, tempus ut sapien. Donec suscipit est id enim facilisis aliquam. Aliquam eros urna, ornare eget semper sit amet, posuere sit amet dolor. Etiam tellus lectus, dignissim non sollicitudin vitae, iaculis at neque. Curabitur nec sem vel felis iaculis iaculis quis nec massa. Suspendisse et lorem orci. Ut ut nunc lectus. Duis dignissim, massa pharetra cursus rutrum, quam ligula ullamcorper libero, sed tincidunt erat nulla ullamcorper sapien. In malesuada rhoncus massa id egestas. Suspendisse in nisi nec nunc vestibulum molestie sed in turpis.";

    long totalNumberOfItems = 500000;
    ExecutorService executor = Executors.newFixedThreadPool(20);

    public class WithDatabase {
        Mungbean db;

        public Database create() {
            db = new Mungbean("localhost", 27017);
            return db.openDatabase(new ObjectId().toHex());
        }

        public void destroy() {
            context.dbAdmin().dropDatabase();
            db.close();
        }

        public void databaseCanBeAccessed() throws InterruptedException {
            final DBCollection<Map<String, Object>> collection = context.openCollection("foo");
            StopWatch timer = new StopWatch();
            for (int a = 0; a < totalNumberOfItems / 100; a++) {
                executor.submit(new Runnable() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void run() {
                        try {
                            Map<String, Object> docs[] = new Map[100];
                            for (int a = 0; a < 100; a++) {
                                docs[a] = new HashMap<String, Object>() {
                                    {
                                        put("foo", "bar");
                                        put("content", contentString);
                                    }
                                };
                            }
                            collection.save(docs);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.MINUTES);
            specify(collection.query(new Count(), new Query()), does.equal(totalNumberOfItems));
            long time = timer.millisecondsSinceStart();
            System.out.println("Insert time for " + totalNumberOfItems + " items was " + time + "ms -> " + (totalNumberOfItems / (time / 1000)) + " operations per second.");

            timer = new StopWatch();
            final AtomicInteger count = new AtomicInteger(0);
            collection.query(new Query(), new QueryCallback<Map<String, Object>>() {
                @Override
                public void process(Map<String, Object> item) {
                    if ("bar".equals(item.get("foo"))) {
                        count.incrementAndGet();
                    } else {
                        throw new IllegalStateException("Invalid item: " + item);
                    }
                }
            });
            specify(count.get(), does.equal(totalNumberOfItems));
            long iterateTime = timer.millisecondsSinceStart();
            System.out.println("Iterate " + totalNumberOfItems + " items in " + iterateTime + "ms");
        }
    }
}