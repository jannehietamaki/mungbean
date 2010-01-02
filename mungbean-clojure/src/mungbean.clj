(ns mungbean 
	(:use [clojure.contrib.def :only [defnk]]
	      [mungbean.wrappers :only [wrap-query wrap-update string-to-id]]
	)
)
            
(defnk get-db [name :host "localhost" :port 27017] (.openDatabase (new mungbean.clojure.ClojureMungbean host port) name))

(defn get-collection [db name] (.openCollection db name))

(defn insert [collection doc] (.save collection doc))

(defn delete [collection id] (.delete collection (string-to-id id)))

(defnk update [collection updates :where {} :multiple false] (.update collection (wrap-query where) (wrap-update updates multiple)))

(defnk query [collection :operation nil :where {} :first 0 :items 1000 :order {}]
	(let [query (wrap-query where :first first :items items :order {})]
    	(if-not (nil? operation)
			(.query collection operation query)
			(.query collection query)	
    	)
	)
)

(defn find-one [collection id] (.find collection (string-to-id id)))

(defn get-id [item] (.toHex (item :_id)))
