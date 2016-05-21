(ns flights-logger.core

  (:gen-class)
  (:require [clojure.data.json :as json] 
            [clj-http.client :as client]))

(declare process-res)
(declare get-results)
(declare qpx-express-request-body)
(declare flight-specifications)
(declare get-user-response)
(declare ask-questions)
(def num-itineraries 25)
(def default-passenger-args
  { :adultCount 1
    :infantInLapCount 0
    :infantInSeatCount 0
    :childCount 0
    :seniorCount 0 })

(def prompts [[:origin "Enter the airport code of departure"]
              [:dest "Enter the airport code of arrival"]
              [:date-out "Enter the date of departure (provide
                         in form \"YYYY-MM-DD\")"]
              [:earliest-out "Enter the earliest time of day you would be
                             willing to depart, in 24h format \"HH:MM\""]
              [:latest-out   "Enter the latest time of day you would be
                             willing to depart, in 24h format \"HH:MM\""]
              [:date-back    "Enter the date of return (provide in form
                             \"YYYY-MM-DD\")"]
              [:earliest-back "Enter the earliest time of day you would be
                              willing to return, in 24h format \"HH:MM\""]
              [:latest-back   "Enter the latest time of day you would be
                              willing to return, in 24h format \"HH:MM\""]
              [:max-price     "Enter the maximum price you are willing to pay
                              (e.g. \"USD500.00\")"]
              ])

(def qpx-express-key "IzaSyDRSEMJ2FgABK7D32HFixT22iPWyYL4bLwa")

(def qpx-express-url "https://www.googleapis.com/qpxExpress/v1/trips/search")


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn interactive-session
  "Controls the interactive session when a user is in -i mode"
  []

)
(defn get-user-response
  [question]
  do 
    (println question)
    (flush)
    (read-line)
)
(defn ask-questions [qs] (reduce #(assoc %1 (%2 0) (get-user-response (%2 1))) {} qs))
(defn process-res
  "Processes the response from QPX flights database"
  [res]
  (json/read-str res))

(defn qpx-express-request-body
  "Creates the body of a QPX express request with given arguments."
  [origin dest date-out earliest-out latest-out 
               date-back earliest-back latest-back
               max-price]
  "Makes a request to the engine with specified parameters"
  {:request 
      {:slice 
          [(flight-specifications origin dest date-out earliest-out latest-out)
           (flight-specifications dest origin date-back earliest-back latest-back)]
       :passengers default-passenger-args
       :solutions num-itineraries
       :maxPrice (or max-price "USD500.00")
       :refundable false
      }
  })

(defn get-results
  "Performs a request for matching results from the QPX flights database"
  [origin dest date-out earliest-out latest-out
   date-back earliest-back latest-back max-price]
  (client/post qpx-express-url (json/write-str 
                                   (qpx-express-request-body origin dest 
                                    date-out earliest-out latest-out
                                    date-back earliest-back latest-back
                                    max-price))))

(defn flight-specifications
  "Returns the slice argument for a request to qpx express
   with default Coach, and entirely permissive departure time."
  
  [origin dest date earliest latest]
  (let [shared { :origin origin
                 :destination dest
                 :date date
                 :preferredCabin "COACH"
               }]
  (if (and (nil? earliest) (nil? latest))
        shared
        (merge shared {:permittedDepartureTime
                        {:earliestTime (or earliest "00:00")
                         :latestTime (or latest "24:00")}}))))


