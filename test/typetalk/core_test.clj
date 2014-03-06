(ns typetalk.core-test
  (:require [clojure.test :refer :all]
            [typetalk.core :refer :all])
  (:use [environ.core]))

(def typetalk-creds
  {:client_id (env :typetalk-client-id)
   :client_secret (env :typetalk-client-secret)})

(deftest a-test
  (testing "get-access-token gets an access token."
    (let [token (get-access-token (:client_id typetalk-creds)
                                  (:client_secret typetalk-creds)
                                  "topic.read,topic.post,my")]
      (is (not (nil? (token "access_token")))))))

(deftest test-get-profile
  (let [token (get-access-token (:client_id typetalk-creds)
                                (:client_secret typetalk-creds)
                                "my")
        access_token (token "access_token")]
    (testing "profile has name and some other properties"
      (let [profile (get-profile access_token)]
        (is (not (nil? (profile "account"))))
        (is (not (nil? (get-in profile ["account" "name"]))))
        (is (not (nil? (get-in profile ["account" "fullName"]))))
        (is (not (nil? (get-in profile ["account" "imageUrl"]))))
        ))))

(deftest test-get-topics
  (let [token (get-access-token (:client_id typetalk-creds)
                                (:client_secret typetalk-creds)
                                "my")
        access_token (token "access_token")]
    (testing "profile has name and some other properties"
      (let [res (get-topics access_token)]
        (is (not (nil? (res "topics"))))
        (is (> (count (res "topics")) 0))
        (doseq [topic (res "topics")]
          (is (not (nil? (topic "topic"))))
          (is (not (nil? (get-in topic ["topic" "id"]))))
          (is (not (nil? (get-in topic ["topic" "name"]))))
          )))))
