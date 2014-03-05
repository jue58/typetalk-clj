(ns typetalk.core
  (:require [clj-http.client :as http]
            [clj-http.util :as http-util]
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

(defn authorization-url [client_id redirect_uri scope]
  (format "https://typetalk.in/oauth2/authorize?client_id=%s&redirect_uri=%s&scope=code"
          (http-util/url-encode client_id)
          (http-util/url-encode redirect_uri)))

(defn refresh-access-token [client_id client_secret refresh_token]
  (let [res (http/post
              "https://typetalk.in/oauth2/access_token"
              {:form-params
                {:client_id client_id
                 :client_secret client_secret
                 :grant_type "refresh_token"
                 :refresh_token refresh_token}})
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

(defn get-topics [access_token]
  "Fetch topics"
  (let [res (http/get
              "https://typetalk.in/api/v1/topics"
              {:query-params {:access_token access_token}})]
    (if (= 200 (:status res))
      (json/read-str (:body res)))))

(defn get-posts [access_token topic]
  (let [res (http/get
              (str "https://typetalk.in/api/v1/topics/" (topic "id"))
              {:query-params {:access_token access_token}})]
    (if (= 200 (:status res))
      (json/read-str (:body res)) "posts")))

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

(defn delete-post [access_token post]
  (let [res (http/delete
              (str "https://typetalk.in/api/v1/topics/" (post "topicId") "/posts/" (post "id"))
              {:query-params {:access_token access_token}})]
    (if (= 200 (:status res))
      (json/read-str (:body res)))))

(defn create-like [access_token post]
  "Adds LIKE to a post
   scope: topic.post"
  (let [res (http/post
              (format "https://typetalk.in/api/v1/topics/%s/posts/%s/like" (post "topicId") (post "id"))
              {:headers {"Authorization" (str "Bearer " access_token)}})]
    (if (= (:status res) 200)
      (json/read-str (:body res)))))

(defn delete-like [access_token post]
  "Deletes LIKE to a post
   scope: topic.post"
  (let [res (http/delete
              (format "https://typetalk.in/api/v1/topics/%s/posts/%s/like" (post "topicId") (post "id"))
              {:headers {"Authorization" (str "Bearer " access_token)}})]
    (if (= (:status res) 200)
      (json/read-str (:body res)))))

(defn create-favorite [access_token topic]
  "scope: my"
  (let [res (http/post
              (format "https://typetalk.in/api/v1/topics/%s/favorite" (topic "id"))
              {:headers {"Authorization" (str "Bearer " access_token)}})]
    (if (= (:status res) 200)
      (json/read-str (:body res)))))

(defn delete-favorite [access_token topic]
  "scope: my"
  (let [res (http/delete
              (format "https://typetalk.in/api/v1/topics/%s/favorite" (topic "id"))
              {:headers {"Authorization" (str "Bearer " access_token)}})]
    (if (= (:status res) 200)
      (json/read-str (:body res)))))

(defn get-notifications [access_token]
  "scope: my"
  (let [res (http/get
              (str "https://typetalk.in/api/v1/notifications/status")
              {:headers {"Authorization" (str "Bearer " access_token)}})]
    (if (= 200 (:status res))
      (json/read-str (:body res)))))

(defn open-notifications [access_token topic &]
  "scope: my"
  (let [res (http/put
              (str "https://typetalk.in/api/v1/bookmark/open")
              {:headers {"Authorization" (str "Bearer " access_token)}})]
    (if (= 200 (:status res))
      (json/read-str (:body res)))))

(defn mark-topic-as-read [access_token topic]
  "scope: my"
  (let [res (http/put
              (str "https://typetalk.in/api/v1/bookmark/open")
              {:headers {"Authorization" (str "Bearer " access_token)}}
              {:form-params {:topicId (topic "id")}})]
    (if (= 200 (:status res))
      (json/read-str (:body res)))))

(defn mark-post-as-read [access_token post]
  "scope: my"
  (let [res (http/put
              (str "https://typetalk.in/api/v1/bookmark/open")
              {:headers {"Authorization" (str "Bearer " access_token)}}
              {:form-params {:topicId (post "topicId") :postId (post "id")}})]
    (if (= 200 (:status res))
      (json/read-str (:body res)))))

(defn get-mentions [access_token &options]
  (println "not implemented"))

(defn mark-mention-as-read [access_token mention]
  (println "not implemented"))

