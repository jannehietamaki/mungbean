(ns mungbean.wrappers
	(:use [clojure.contrib.def :only [defnk]])	
)

(defnk wrap-query [where :first 0 :items 1000 :order {}]
; TODO support ordering
     (new mungbean.clojure.ClojureQuery where first items)
)

(defn wrap-update [updates]
     (new mungbean.clojure.ClojureUpdate updates)
)
