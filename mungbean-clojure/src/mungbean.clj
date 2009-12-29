(ns mungbean)

(defn get-db [name]
(.openDatabase (new mungbean.clojure.ClojureMungbean "localhost" 27017) name)
)

(defn get-collection [db name]
(.openCollection db name)
)

(defn save [collection, doc]
(.save collection doc)
)

(defn delete [collection, query]
(.delete collection query)
)

(defn update [collection, query, doc, upsert]
(.update collection query doc upsert)
)

(defn query [collection, rules, first, items]
(seq (.query collection rules first items))
)

(defn find-one [collection, id]
(.find collection id)
)

(defn command [db, cmd]
(.command db cmd)
)