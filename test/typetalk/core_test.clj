(ns typetalk.core-test
  (:require [clojure.test :refer :all]
            [typetalk.core :refer :all])
  (:use [environ.core]))

(def typetalk-creds
  {:client_id (env :typetalk-client-id)
   :client_secret (env :typetalk-client-secret)})

(deftest a-test
  (testing "get-access-token gets an access token."
    (println (:client_id typetalk-creds))    
    (println (:client_secret typetalk-creds))
    (let [token (get-access-token (:client_id typetalk-creds)
                                  (:client_secret typetalk-creds)
                                  "topic.read,topic.post,my")]
      (is (not (nil? (token "access_token")))))))


