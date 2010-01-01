(ns mungbean_test
  (:use [clojure.test])
  (:require [mungbean :as mongo])   
)

(declare coll)

(defn insert-test-data [count & values] 
   (doseq [n (take count (cycle values))] (mongo/insert coll {:foo n}))
)

(defmacro with-mungo [name & body]
	`(let [db# (mongo/get-db (.toHex (new mungbean.ObjectId)))]
	    (let [collection# (mongo/get-collection db# ~name)]
	       (binding [coll collection#]	    
               (do ~@body)
		   )
	    )
	    (.dropDatabase (.dbAdmin db#))
	)
)

(deftest save-item-read-back-and-delete
   (with-mungo "foo"
       (let [id ((mongo/insert coll {:foo "bar"}) :_id)]
           (is (= ((mongo/find-one coll id) :foo) "bar"))
	       (mongo/delete coll id)
       )
   )
)

(deftest generate-items-and-query
   (with-mungo "foo"
       (insert-test-data 10 "foo" "bar" "zoo")
       (is (= 3 (count (mongo/query coll :where {:foo "bar"}))))
   )
)

(deftest more-advanced-query
   (with-mungo "foo"
       (insert-test-data 10 1 3 5)       
       (is (= 6 (count (mongo/query coll :where {:foo {:$gt 2}}))))
   )
)

(deftest update-in-place
   (with-mungo "foo"
       (insert-test-data 10 1 3 5)
       (mongo/update coll {} {:$inc {:foo 3}})
       (is (= 3 (count (mongo/query coll :where {:foo 8}))))
   )
)
