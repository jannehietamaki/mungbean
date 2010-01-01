(ns mungbean
	(:use clojure.contrib.def)
)

(defnk get-db [name :host "localhost" :port 27017] (.openDatabase (new mungbean.clojure.ClojureMungbean host port) name))

(defn get-collection [db name] (.openCollection db name))

(defn insert [collection doc] (.save collection doc))

(defn delete [collection query] (.delete collection query))

(defn update [collection query updates] (.update collection query updates))

(defnk query [collection :where {} :first 0 :items 1000] (.query collection where first items))

(defn find-one [collection id] (.find collection id))

(defn command [db cmd] (.command db cmd))