(ns mungbean.wrappers
	(:use [clojure.contrib.def :only [defnk]])	
)

(defnk wrap-query [where :first 0 :items 1000 :order {}]
; TODO support ordering
     (new mungbean.clojure.ClojureQuery where first items)
)

(defn wrap-update [updates multiple upsert]
     (new mungbean.clojure.ClojureUpdate updates multiple upsert)
)

(defn string-to-id [id] 
    (if-not (instance? mungbean.ObjectId id) 
        (new mungbean.ObjectId id)
        id
    )    
)
