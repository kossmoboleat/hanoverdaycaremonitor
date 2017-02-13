(ns daycaremap.checker
  (:require [clj-http.client :as client]
            [clojure.set :as set]
            [daycaremap.config :refer [env]]
            [daycaremap.daycare :as daycare]))

(defn send-mail [differences subject to]
  (let [mailgun-api-key (env :mailgun-api-key)
        mail-sender-domain (env :mail-sender-domain)
        differences-str (apply str (interpose ", " differences))]
    (prn (str "sending mail with body: " differences-str))
    (client/post (str "https://api.mailgun.net/v3/" mail-sender-domain "/messages")
                 {:basic-auth  ["api" mailgun-api-key]
                  :form-params {:from    (str "Daycare Checker <checker@" mail-sender-domain ">")
                                :to      to
                                :subject subject
                                :text    (str "New are the daycares: " differences-str)}})))

; TODO add detail links to new daycares
(defn get-current-open-daycares []
  (let [current-daycares (map #(get % "anzeigename") (daycare/get-daycares))]
    (println "current daycares: " (apply str (interpose ", " current-daycares)))
    current-daycares))

(defn check []
  (let [known-daycares (env :known-daycares)
        current-daycares (into #{} (get-current-open-daycares))
        differences (set/difference current-daycares known-daycares)
        recipient-email-addresses (env :recipient-email-addresses)]
    (when (not-empty differences)
      (send-mail differences "new daycares" recipient-email-addresses))))