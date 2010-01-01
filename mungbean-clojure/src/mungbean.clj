(ns mungbean 
	(:use [clojure.contrib.def :only [defnk]]
	      [mungbean.wrappers :only [wrap-query wrap-update]]
	)
)
            

(defnk get-db [name :host "localhost" :port 27017] (.openDatabase (new mungbean.clojure.ClojureMungbean host port) name))

(defn get-collection [db name] (.openCollection db name))

(defn insert [collection doc] (.save collection doc))

(defn delete [collection id] (.delete collection id))

(defnk update [collection updates :where {}] (.update collection (wrap-query where) (wrap-update updates)))

(defnk query [collection :operation nil :where {} :first 0 :items 1000 :order {}]
	(let [query (wrap-query where :first first :items items :order {})]
    	(if-not (nil? operation)
			(.query collection operation query)
			(.query collection query)	
    	)
	)
)

(defn find-one [collection id] (.find collection id))

(defn command [db cmd] (.command db cmd))

