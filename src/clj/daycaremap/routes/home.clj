(ns daycaremap.routes.home
  (:require [daycaremap.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [clojure.java.io :as io]
            [daycaremap.daycare :as daycare])
  (:import [org.joda.time DateTimeZone]))

(defn home-page []
  (layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
           (GET "/" [] (home-page))
           (GET "/about" [] (about-page))
           (GET "/daycares" [] (daycare/get-daycares)))