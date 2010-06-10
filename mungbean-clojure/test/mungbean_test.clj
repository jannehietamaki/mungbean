(ns mungbean_test
  (:use [clojure.test])
  (:require [mungbean :as mongo]
  	        [mungbean.aggregation :as aggregation]
            [mungbean.admin :as admin]
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

(defstruct teststruct :name :age :city :country)

(deftest save-and-load-struct
   (with-mungo "struct"
      (mongo/insert coll (struct teststruct "James Bond" 42  "London" "UK"))
      (is (= "James Bond" ((mongo/query-one coll {:name "James Bond"}) :name)))
   )
)


(deftest save-and-load-structural-map
   (with-mungo "foo"
      (mongo/insert coll {:foo {:bar 2, :zoo 3}, :name "James Bond"})
      (is (= {:foo {:bar 2, :zoo 3}, :name "James Bond"} (dissoc (mongo/query-one coll {:name "James Bond"}) :_id)))
   )
)

(deftest save-item-read-back-and-delete
   (with-mungo "foo"
       (let [id (mongo/get-id (mongo/insert coll {:foo "bar"}))]
           (is (= ((mongo/find-one coll id) :foo) "bar"))
	       (mongo/delete-one coll id)
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
       (is (= ["bar" "foo" "zoo"] (mongo/query coll :operation (aggregation/get-distinct :foo) :order {:foo :asc} )))
   )
)


(deftest more-advanced-query
   (with-mungo "foo"
       (insert-test-data 10 1 3 5)       
       (is (= 6 (count (mongo/query coll :where {:foo {:$gt 2}}))))
   )
)

(deftest admin-interface
  (with-mungo "foo"
    (admin/ensure-index coll [:name] :unique true :dropDups true)
  )
)

(deftest function-can-be-given-to-iterate-items
   (with-mungo "foo"
       (insert-test-data 10 1 3 5)       
       (is (= [1 1 1 1 3 3 3 5 5 5] (mongo/query coll :order {:foo :asc} :function (fn [item] (item :foo)))))
   )
)

(deftest ordered-items-can-be-queried
   (with-mungo "foo"
       (admin/ensure-index coll [:foo])
       (insert-test-data 10 1 3 5)       
       (is (= [1 1 1 1 3 3 3 5 5 5] (mongo/query coll :order {:foo :asc} :function (fn [item] (item :foo)))))
       (is (= [5 5 5 3 3 3 1 1 1 1] (mongo/query coll :order {:foo :desc} :function (fn [item] (item :foo)))))
   )
)

(deftest update-in-place
   (with-mungo "foo"
       (insert-test-data 10 1 3 5)
       (mongo/update coll {:$inc {:foo 3}, :$set {:bar "zoo"}} :multiple true)
       (is (= 3 (count (mongo/query coll :where {:foo 8}))))
       (is (= 10 (count (mongo/query coll :where {:bar "zoo"}))))
   )
)

(deftest do-upsert
   (with-mungo "foo"
       (mongo/update coll {:$set {:bar "zoo"}} :where {:zoo 1} :upsert true)
       (is (= 1 (count (mongo/query coll :where {:zoo 1, :bar "zoo"}))))
   )
)


(deftest insert-structured-object
  (with-mungo "foo"
      (mongo/insert coll {"id" "123"})
      (mongo/update coll {"id" "123", "zzz" [1,2]} :where {"id" "123"})
      (is (= 1 (count (mongo/query coll :where {"id" "123"}))))
   )  
)
