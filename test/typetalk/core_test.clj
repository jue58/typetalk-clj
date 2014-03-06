(ns typetalk.core-test
  (:require [clojure.test :refer :all]
            [typetalk.core :refer :all])
  (:use [environ.core]))

(def typetalk-creds
  {:client_id (env :typetalk-client-id)
   :client_secret (env :typetalk-client-secret)})

(def token
   (get-access-token (:client_id typetalk-creds)
                     (:client_secret typetalk-creds)
                     "topic.read,topic.post,my"))

(def access-token (token "access_token"))

(deftest a-test
  (testing "get-access-token gets an access token."
    (let [token (get-access-token (:client_id typetalk-creds)
                                  (:client_secret typetalk-creds)
                                  "topic.read,topic.post,my")]
      (is (not (nil? (token "access_token")))))))

(deftest test-get-profile
  (testing "profile has name and some other properties"
    (let [profile (get-profile access-token)]
      (is (not (nil? (profile "account"))))
      (is (not (nil? (get-in profile ["account" "name"]))))
      (is (not (nil? (get-in profile ["account" "fullName"]))))
      (is (not (nil? (get-in profile ["account" "imageUrl"]))))
      )))

(deftest test-get-topics
  (testing "get-topics"
    (let [res (get-topics access-token)]
      (is (not (nil? (res "topics"))))
      (is (> (count (res "topics")) 0))
      (doseq [topic (res "topics")]
        (is (not (nil? (topic "topic"))))
        (is (not (nil? (get-in topic ["topic" "id"]))))
        (is (not (nil? (get-in topic ["topic" "name"]))))
        ))))

(deftest test-get-posts
  (testing "get-posts"
    (let [topics ((get-topics access-token) "topics")
          topic ((first topics) "topic")
          res (get-posts access-token topic)]
      (is (not (nil? (res "posts"))))
      (is (> (count (res "posts")) 0))
      (doseq [post (res "posts")]
        (is (not (nil? (post "id"))))
        (is (not (nil? (post "topicId"))))
        (is (not (nil? (post "message"))))
        (is (not (nil? (post "account"))))
        (is (not (nil? (post "url"))))
        ))))
