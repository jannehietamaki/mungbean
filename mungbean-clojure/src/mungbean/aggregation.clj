(ns mungbean.aggregation)
(defn get-count [] (new mungbean.protocol.command.Count))
; TODO we need clojure specific distinct, this returns java lists
(defn get-distinct [field] (new mungbean.protocol.command.Distinct (.getName field)))
