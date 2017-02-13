(ns daycaremap.daycare
  (:require
    [clj-http.client :as client]
    [cheshire.core :as cheshire]
    [ring.util.http-response :as response]
    [daycaremap.config :refer [env]]
    [clojure.string :as str]))

(defn obtain-sid []
  (let [resp (client/get "http://hannover.betreuungsboerse.net")]
    (when (:status resp)
      (second (re-find #".*var sid = \"(.*)\"" (:body resp))))))

(def url (str "http://hannover.betreuungsboerse.net/conect/portal/core/ajax.php?sid="
              (obtain-sid)
              "&controller=net::conne::conect::modules::BetreuungsboerseMap::Controller"))

; TODO add position as parameter
; TODO add distance as parameter
(defn get-daycares []
  (let [latitude (env :latitude)
        longitude (env :longitude)
        distance (str (env :distance))
        resp (client/post url {:form-params {(keyword "filterRules[Categories][0][coar_pid]")      "3"
                                             (keyword "filterRules[Categories][1][coar_pid]")      "4"
                                             (keyword "filterRules[Categories][2][coar_pid]")      "5"
                                             (keyword "filterRules[Categories][3][coar_pid]")      "22"
                                             (keyword "filterRules[Categories][4][coar_pid]")      "23"
                                             (keyword "filterRules[Categories][5][coar_pid]")      "24"
                                             (keyword "filterRules[Categories][6][coar_pid]")      "25"
                                             (keyword "filterRules[POINearPosition][0][distance]") distance
                                             (keyword "filterRules[POINearPosition][0][lat]")      latitude
                                             (keyword "filterRules[POINearPosition][0][lng]")      longitude
                                             (keyword "method")                                    "getPOIs"}}

                          :as :json)]
    (filter #(and (contains? %1 "plaetze")
                  (not (str/starts-with? (get %1 "plaetze") "0")))
            (map #(select-keys % ["latitude"
                                  "longitude"
                                  "plaetze"
                                  "additionalDivs"
                                  "anzeigename"])
                 (cheshire/parse-string
                   (:body resp))))))