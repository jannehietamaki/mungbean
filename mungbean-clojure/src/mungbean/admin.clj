(ns mungbean.admin
  (:use [clojure.contrib.def :only [defnk]])
  )
(defnk ensure-index [collection fields :unique false :dropDups false] 
  (.ensureIndex (.collectionAdmin collection) unique dropDups fields)
  )

(defn collection-names [db]
  (.collectionNames (.dbAdmin db))
)