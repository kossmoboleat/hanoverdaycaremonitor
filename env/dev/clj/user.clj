(ns user
  (:require [mount.core :as mount]
            daycaremap.core))

(defn start []
  (mount/start-without #'daycaremap.core/repl-server))

(defn stop []
  (mount/stop-except #'daycaremap.core/repl-server))

(defn restart []
  (stop)
  (start))


