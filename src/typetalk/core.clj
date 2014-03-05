(ns typetalk.core
  (:require [clj-http.client :as http]
            [clojure.data.json :as json]))

(defn get-access-token [client_id client_secret scope]
  (let [res (http/post
              "https://typetalk.in/oauth2/access_token"
              {:form-params
                {:client_id client_id
                 :client_secret client_secret
                 :grant_type "client_credentials"
                 :scope scope}})
        status (:status res)
        body (:body res)]
    (if (= status 200)
      (json/read-str body))))

(defn get-profile [access_token]
  "Fetch the user profile of the user who got the given access token"
  (let [res (http/get
              "https://typetalk.in/api/v1/profile"
              {:query-params {:access_token access_token}})]
    (if (= 200 (:status res))
      (json/read-str (:body res)))))

(defn- topics-response->topics [json]
  (let [arr (json "topics")]
    (map #(% "topic") arr)))

(defn get-topics [access_token]
  "Fetch topics"
  (let [res (http/get
              "https://typetalk.in/api/v1/topics"
              {:query-params {:access_token access_token}})]
    (if (= 200 (:status res))
      (-> (json/read-str (:body res))
        (topics-response->topics)))))

(defn get-posts [access_token topic]
  (let [res (http/get
              (str "https://typetalk.in/api/v1/topics/" (topic "id"))
              {:query-params {:access_token access_token}})]
    (if (= 200 (:status res))
      ((json/read-str (:body res)) "posts"))))

(defn create-post [access_token topic message]
  (let [res (http/post
              (str "https://typetalk.in/api/v1/topics/" (topic "id"))
              {:form-params
                {:message message :access_token access_token}})]
    (if (= (:status res) 200)
      (json/read-str (:body res)))))

(defn get-post [access_token post]
  (let [res (http/get
              (str "https://typetalk.in/api/v1/topics/" (post "topicId") "/posts/" (post "id"))
              {:query-params {:access_token access_token}})]
    (if (= 200 (:status res))
      (json/read-str (:body res)))))

(defn delete-post [access_id post]
  (println "not implemented"))

(defn create-like [access_id post]
  (println "not implemented"))

(defn delete-like [access_id post]
  (println "not implemented"))

(defn create-favorite [access_id topic]
  (println "not implemented"))

(defn delete-favorite [access_id topic]
  (println "not implemented"))

(defn get-notifications [access_id]
  (println "not implemented"))

(defn open-notifications [access_id]
  (println "not implemented"))

(defn mark-topic-as-read [access_id topic]
  (println "not implemented"))

(defn mark-post-as-read [access_id post]
  (println "not implemented"))

(defn get-mentions [access_id &options]
  (println "not implemented"))

(defn mark-mention-as-read [access_id mention]
  (println "not implemented"))

