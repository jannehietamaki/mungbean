(ns mungbean_test
  (:use
       [clojure.test]
       [mungbean]
  )   
)

(deftest insert-item-read-back-and-delete
	(let [mungo (get-collection (get-db "foobar") "foo")]
		(let [id ((insert mungo {:foo "bar"}) :_id)]
		   (find-one mungo id)
		   (delete mungo id)
		)
	) 
)
