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

(deftest test-create-post
  (testing "create-post"
    (let [topics ((get-topics access-token) "topics")
          topic ((first topics) "topic")
          res (create-post access-token topic (str "テストです。こんにちは。" (java.util.Date.)))]
      (is (not (nil? (res "post"))))
      (is (not (nil? (res "topic"))))
      )))

(defn- first-topic [access-token]
  (let [topics ((get-topics access-token) "topics")]
     ((first topics) "topic")))

(defn- first-topic-posts [access-token]
  (->> (first-topic access-token)
       (get-posts access-token)
       (#(% "posts"))))

(defn- first-topic-posts-count [access-token]
  (count (first-topic-posts access-token)))

(defn- first-post [access-token]
  (let [topic (first-topic access-token)
        posts (get-posts access-token topic)]
    (first (posts "posts"))))

(defn likes [post]
  (post "likes"))

(deftest test-get-post
  (testing "get-post"
    (let [post (first-post access-token)
          res (get-post access-token post)]
      (is (not (nil? (res "post"))))
      (is (= (get-in res ["post" "id"]) (post "id")))
      )))

(deftest test-delete-post
  (testing "delete-post"
    (let [n (first-topic-posts-count access-token)
          post (first-post access-token)
          res  (delete-post access-token post)]
      (is (= (dec n) (first-topic-posts-count access-token)))
      )))

(deftest test-create-like
  (testing "create-like"
    (let [post (first-post access-token)
          res   (create-like access-token post)
          post2 ((get-post access-token post) "post")]
      (is (< (count (likes post))
             (count (likes post2))))
      )))

(deftest test-delete-like
  (testing "delete-like"
    (let [post (first-post access-token)
          res  (delete-like access-token post)
          post2 ((get-post access-token post) "post")]
      (is (> (count (likes post)))
             (count (likes post2)))
      )))

(deftest test-create-favorite
  (testing "create-favorite"
    (let [topic (first-topic access-token)
          res   (create-favorite access-token topic)
          topic2 (first-topic access-token)]
      (println topic2)
      )))

(deftest test-delete-favorite
  (testing "delete-favorite"
    (let [topic (first-topic access-token)
          res (delete-favorite access-token topic)]
      (println res)
      )))

(deftest test-get-notifications
  (testing "get-notifications"
    (let [res (get-notifications access-token)]
      (println res)
      )))

(deftest test-open-notifications
  (testing "open-notifications"
    (let [topic (first-topic access-token)
          res (open-notifications access-token topic)]
      (println res))))

(deftest test-mark-topic-as-read
  (testing "mark-topic-as-read"
    (let [topic (first-topic access-token)
          res (mark-topic-as-read access-token topic)]
      (println res))))

(deftest test-mark-post-as-read
  (testing "mark-post-as-read"
    (let [post (first-post access-token)
          res (mark-post-as-read access-token post)]
      (println res))))

(deftest test-get-mentions
  (testing "get-mentions"
    (let [res (get-mentions access-token)]
      (println res)
      )))

(defn- get-first-mention [access-token]
  (first (get-mentions access-token)))

(deftest test-mark-mention-as-read
  (testing "mark-mention-as-read"
    (let [mention (get-first-mention access-token)
           res (get-mentions access-token)]
      (println res)
      )))
