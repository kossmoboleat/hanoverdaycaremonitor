(ns daycaremap.core
  (:require [daycaremap.handler :as handler]
            [luminus.repl-server :as repl]
            [luminus.http-server :as http]
            [daycaremap.checker-scheduler :as scheduler]
            [daycaremap.config :refer [env]]
            [clojure.tools.cli :refer [parse-opts]]
            [clojure.tools.logging :as log]
            [clj-time.core :as t]
            [clj-time.periodic :refer [periodic-seq]]
            [chime]
            [daycaremap.daycare :as daycare]
            [daycaremap.checker :as checker]
            [mount.core :as mount])
  (:import [org.joda.time DateTimeZone])
  (:gen-class))

(def cli-options
  [["-p" "--port PORT" "Port number"
    :parse-fn #(Integer/parseInt %)]])

(mount/defstate ^{:on-reload :noop}
                http-server
                :start
                (http/start
                  (-> env
                      (assoc :handler (handler/app))
                      (update :port #(or (-> env :options :port) %))))
                :stop
                (http/stop http-server))

(mount/defstate ^{:on-reload :noop}
                repl-server
                :start
                (when-let [nrepl-port (env :nrepl-port)]
                  (repl/start {:port nrepl-port}))
                :stop
                (when repl-server
                  (repl/stop repl-server)))

(mount/defstate ^{:on-reload :noop}
                checker-scheduler
                :start
                (scheduler/start)
                :stop
                (scheduler/stop checker-scheduler))

; TODO start the checker properly as mount component
;(chime/chime-at (->> (periodic-seq (.. (t/now)
;                                       (withZone (DateTimeZone/forID "Europe/Berlin"))
;                                       (withTime 8 0 0 0))
;                                   (-> 1 t/hours)))
;
;                (fn [time]
;                  (println "Chiming at" time)
;
;                  (log/info "Checking daycares...")
;                  (checker/check))
;
;                {:on-finished (fn []
;                                (println "Schedule finished."))})

(defn stop-app []
  (doseq [component (:stopped (mount/stop))]
    (log/info component "stopped"))
  (shutdown-agents))

(defn start-app [args]
  (doseq [component (-> args
                        (parse-opts cli-options)
                        mount/start-with-args
                        :started)]
    (log/info component "started"))
  (.addShutdownHook (Runtime/getRuntime) (Thread. stop-app)))

(defn -main [& args]
  (start-app args))
