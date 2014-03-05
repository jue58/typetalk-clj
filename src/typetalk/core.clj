(ns typetalk.core
  (:require [clj-http.client :as http]
            [clojure.data.json :as json]))

(defn access_token [client_id client_secret scope]
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

(defn profile [access_token]
  "Fetch the user profile of the user who got the given access token"
  (let [res (http/get
              "https://typetalk.in/api/v1/profile"
              {:query-params {:access_token access_token}})]
    (if (= 200 (:status res))
      (json/read-str (:body res)))))

(defn- topics-response->topics [json]
  (let [arr (json "topics")]
    (map #(% "topic") arr)))

(defn topics [access_token]
  "Fetch topics"
  (let [res (http/get
              "https://typetalk.in/api/v1/topics"
              {:query-params {:access_token access_token}})]
    (if (= 200 (:status res))
      (-> (json/read-str (:body res))
        (topics-response->topics)))))

(defn posts [access_token topic]
  (let [res (http/get
              (str "https://typetalk.in/api/v1/topics/" (topic "id"))
              {:query-params {:access_token access_token}})]
    (if (= 200 (:status res))
      ((json/read-str (:body res)) "posts"))))

(defn new-post [access_token topic message]
  (let [res (http/post
              (str "https://typetalk.in/api/v1/topics/" (topic "id"))
              {:form-params
                {:message message :access_token access_token}})]
    (if (= (:status res) 200)
      (json/read-str (:body res)))))

(defn post [access_token topic post]
  (let [res (http/get
              (str "https://typetalk.in/api/v1/topics/" (topic "id") "/posts/" (post "id"))
              {:query-params {:access_token access_token}})]
    (if (= 200 (:status res))
      (json/read-str (:body res)))))
