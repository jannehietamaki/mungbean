(ns mungbean)

(defn get-db [name] (.openDatabase (new mungbean.clojure.ClojureMungbean "localhost" 27017) name))

(defn get-collection [db name] (.openCollection db name))

(defn insert [collection, doc] (.save collection doc))

(defn delete [collection, query] (.delete collection query))

(defn update [collection, query, updates] (.update collection query updates))

(defn query [collection, rules, first, items] (.query collection rules first items))

(defn find-one [collection, id] (.find collection id))

(defn command [db, cmd] (.command db cmd))