(ns mungbean.wrappers
  (:use [clojure.contrib.def :only [defnk]])	
  )

(defnk wrap-query [where :skip 0 :items 1000 :order {}]
  (new mungbean.clojure.ClojureQuery where skip items order)
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
