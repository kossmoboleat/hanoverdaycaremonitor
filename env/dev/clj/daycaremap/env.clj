(ns daycaremap.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [daycaremap.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[daycaremap started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[daycaremap has shut down successfully]=-"))
   :middleware wrap-dev})
