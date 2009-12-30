(ns mungbean_test
  (:use [clojure.test])
  (:require [mungbean :as mongo])   
)

(declare coll)

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
       (doseq [n (take 15 (cycle ["foo" "bar" "zoo"]))]
           (mongo/insert coll {:foo n})
       )       
       (is (= 5 (count (mongo/query coll {:foo "bar"} 0 100))))
   )
)
