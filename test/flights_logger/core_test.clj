(ns flights-logger.core-test
  (:require [clojure.test :refer :all]
            [flights-logger.core :refer :all]
            [clojure.pprint :as pprint]))

(def expected-query-body
  {:request {:slice [{:origin "SFO"
                     :destination "STL"
                     :date "2016-04-01"
                     :preferredCabin "COACH"}
                     {:origin "STL"
                      :destination "SFO"
                      :date "2016-04-04"
                      :preferredCabin "COACH"}]
             :passengers {:adultCount 1
                          :infantInLapCount 0
                          :infantInSeatCount 0
                          :childCount 0
                          :seniorCount 0}
             :solutions 25
             :maxPrice "USD500.00"
             :refundable false
             }
   }
                      )

(deftest correct-query-body
  (testing "qpx-express-request-body creates a correct request"
    (is (= expected-query-body (qpx-express-request-body
                                 "SFO" "STL" "2016-04-01" nil nil 
                                 "2016-04-04" nil nil "USD500.00")))))
