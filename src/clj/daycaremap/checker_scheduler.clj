(ns daycaremap.checker-scheduler
  (:require [clojure.tools.logging :as log]
            [clj-time.core :as t]
            [daycaremap.checker :as checker]
            [clj-time.periodic :refer [periodic-seq]]
            [chime])
  (:import (org.joda.time DateTimeZone)))

(defn start []
  (chime/chime-at (->> (periodic-seq (.. (t/now)
                                         (withZone (DateTimeZone/forID "Europe/Berlin"))
                                         (withTime 8 0 0 0))
                                     (-> 1 t/hours)))

                  (fn [time]
                    (println "Chiming at" time)

                    (log/info "Checking daycares...")
                    (checker/check))

                  {:on-finished (fn []
                                  (println "Schedule finished."))}))


; TODO properly stop the scheduled chimes here

(defn stop [server]
  (log/info "Should stop chiming now"))