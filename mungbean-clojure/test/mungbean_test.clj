(ns mungbean_test
  (:use [clojure.test])
  (:require [mungbean :as mongo]
  	        [mungbean.aggregation :as aggregation] 
  )   
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

(deftest generate-items-and-get-count
   (with-mungo "foo"
       (insert-test-data 10 "foo" "bar" "zoo")
       (is (= 3 (mongo/query coll :operation (aggregation/get-count) :where {:foo "bar"})))
   )
)


(deftest generate-items-and-get-distinct
   (with-mungo "foo"
       (insert-test-data 10 "foo" "bar" "zoo")
       (is (= ["bar" "foo" "zoo"] (mongo/query coll :operation (aggregation/get-distinct :foo))))
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
       (mongo/update coll {:$inc {:foo 3}} :multiple true)
       (is (= 3 (count (mongo/query coll :where {:foo 8}))))
   )
)
