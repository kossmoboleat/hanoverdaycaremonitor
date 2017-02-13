(ns daycaremap.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[daycaremap started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[daycaremap has shut down successfully]=-"))
   :middleware identity})
