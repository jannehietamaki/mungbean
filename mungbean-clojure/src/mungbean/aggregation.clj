(ns mungbean.aggregation)

(defn get-count [] (new mungbean.protocol.command.Count))
(defn get-distinct [field] (new mungbean.protocol.command.Distinct (.getName field)))
