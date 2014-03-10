(ns typetalk.core
  (:require [clj-http.client :as http]
            [clj-http.util :as http-util]
            [clojure.data.json :as json]))

(defn- get-request [access-token path params]
  (http/get
    (str "https://typetalk.in/api/v1/" path)
    (merge {:headers {"Authorization" (str "Bearer " access-token)}}
           params)))

(defn- post-request [access-token path params]
  (http/post
    (str "https://typetalk.in/api/v1/" path)
    (merge {:headers {"Authorization" (str "Bearer " access-token)}
            :form-params params})))

(defn- get-api [access-token path & params]
  (let [res (get-request access-token path (apply hash-map params))]
    (if (= 200 (:status res))
      (json/read-str (:body res)))))

(defn- post-api [access-token path params]
  (let [res (post-request access-token path params)]
    (if (= (:status res) 200)
      (json/read-str (:body res)))))

(defn delete-api [access-token path]
  (let [res (http/delete
              (str "https://typetalk.in/api/v1/" path)
              {:headers {"Authorization" (str "Bearer " access-token)}})]
    (if (= (:status res) 200)
      (json/read-str (:body res)))))

(defn put-api [access-token path]
  "scope: my"
  (let [res (http/put
              (str "https://typetalk.in/api/v1/" path)
              {:headers {"Authorization" (str "Bearer " access-token)}})]
    (if (= 200 (:status res))
      (json/read-str (:body res)))))


(defn get-access-token [client_id client_secret scope]
  (let [res (http/post
              "https://typetalk.in/oauth2/access_token"
              {:form-params
                {:client_id client_id
                 :client_secret client_secret
                 :grant_type "client_credentials"
                 :scope scope}})]
    (if (= 200 (:status res))
      (json/read-str (:body res)))))

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
                 :refresh_token refresh_token}})]
    (if (= 200 (:status res))
      (json/read-str (:body res)))))


(defn get-profile [access_token]
  "Fetch the user profile of the user who got the given access token"
  (get-api access_token "profile"))

(defn get-topics [access_token]
  "Fetch topics"
  (get-api access_token "topics"))

(defn get-posts [access_token topic]
  (get-api access_token
           (str "topics/" (topic "id"))))

(defn create-post [access-token topic message]
  (post-api access-token
            (str "topics/" (topic "id"))
            {:message message}))

(defn get-post [access_token post]
  (get-api access_token
           (str "topics/" (post "topicId") "/posts/" (post "id"))))

(defn delete-post [access-token post]
  (delete-api access-token
              (str "topics/" (post "topicId") "/posts/" (post "id"))))

(defn create-like [access-token post]
  "Adds LIKE to a post
   scope: topic.post"
  (post-api access-token
            (format "topics/%s/posts/%s/like" (post "topicId") (post "id"))
            {}))

(defn delete-like [access-token post]
  "Deletes LIKE to a post
   scope: topic.post"
  (delete-api access-token
              (format "topics/%s/posts/%s/like" (post "topicId") (post "id"))))

(defn create-favorite [access-token topic]
  "scope: my"
  (post-api access-token
            (format "topics/%s/favorite" (topic "id"))
            {}))

(defn delete-favorite [access-token topic]
  "scope: my"
  (delete-api access-token
              (format "topics/%s/favorite" (topic "id"))))

(defn get-notifications [access_token]
  "scope: my"
  (get-api access_token "notifications/status"))

(defn open-notifications [access-token topic]
  "scope: my"
  (put-api access-token "notifications/open"))

(defn mark-topic-as-read [access-token topic]
  "scope: my"
  (post-api access-token
            "bookmark/save"
            {:topicId (topic "id")}))

(defn mark-post-as-read [access-token post]
  "scope: my"
  (post-api access-token
            "bookmark/save"
            {:topicId (post "topicId") :postId (post "id")}))

(defn get-mentions [access-token & options]
  "scope: my"
  (get-api access-token "mentions" :query-params (apply hash-map options)))

(defn mark-mention-as-read [access_token mention]
  "scope: my"
  (put-api (str "mentions/" (mention "id"))))
