(ns flights-logger.core
  (:gen-class)
  (:require [clojure.data.json :as json] 
            [clj-http.client :as client]))
(def num-solutions
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn qpx-express-request-body
  [origin dest date-out earliest-out latest-out 
               date-back earliest-back earlist-out
               max-price]
  "Makes a request to the engine with specified parameters"
  {:request 
      {:slice 
          [(flight-specifications origin dest date-out earliest-out latest-out)
           (flight-specifications dest origin date-back earliest-back latest-back)]
       :passengers default-passenger-args
       :solutions 25
       :maxPrice (or max-price "USD400.00")
       :refundable false
      }
)

(defn flight-specifications
  "Returns the slice argument for a request to qpx express
   with default Coach, and entirely permissive departure time."
  
  [origin dest date earliest latest]
  (let [shared { :origin origin
                 :dest dest
                 :date date
                 :preferredCabin "COACH"
               }]
  (if (and (?nil earliest) (?nil latest))
        shared
        (merge shared {:permittedDepartureTime
                        {:earliestTime (or earliest "00:00")
                         :latestTime (or latest "24:00")}}))))

 

(def default-passenger-args
  { :adultCount 1
    :infantInLapCount 0
    :infantInSeatCount 0
    :childCount 0
    :seniorCount 0 }
)
