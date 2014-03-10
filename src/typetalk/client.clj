(ns typetalk.client
  (:require [typetalk.core :as core]))


(def get-access-token core/get-access-token)

(def authorization-url core/authorization-url)

(defn refresh-access-token [client_id client_secret access-token]
  (core/refresh-access-token client_id client_secret (access-token "refresh_token")))

(defn- token [access_token]
  (access_token "access_token"))

(defn get-profile [access_token]
  "Fetch the user profile of the user who got the given access token"
  (-> (core/get-profile (token access_token))
      (get "account")))

(defn- ->topics [raw-topics]
  (->> (get raw-topics "topics")
       (map #(get % "topic"))))

(defn get-topics [access_token]
  "Fetch topics"
  (-> (core/get-topics (token access_token))
      (->topics)))

(defn get-posts [access_token topic]
  (-> (core/get-posts (token access_token) topic)
      (get "posts")))

(defn create-post [access_token topic message]
  (core/create-post (token access_token) topic message))

(defn get-post [access_token post]
  (-> (core/get-post (token access_token) post)
      (get "post")))

(defn delete-post [access_token post]
  (core/delete-post (token access_token) post))

(defn create-like [access_token post]
  "Adds LIKE to a post
   scope: topic.post"
  (core/create-like (token access_token) post))

(defn delete-like [access_token post]
  "Deletes LIKE to a post
   scope: topic.post"
  (core/delete-like (token access_token) post))

(defn create-favorite [access_token topic]
  "scope: my"
  (core/create-favorite (token access_token) topic))

(defn delete-favorite [access_token topic]
  "scope: my"
  (core/delete-favorite (token access_token) topic))

(defn get-notifications [access_token]
  "scope: my"
  (core/get-notifications (token access_token)))

(defn open-notifications [access_token topic]
  "scope: my"
  (core/open-notifications (token access_token) topic))

(defn mark-topic-as-read [access_token topic]
  "scope: my"
  (core/mark-topic-as-read (token access_token) topic))

(defn mark-post-as-read [access_token post]
  "scope: my"
  (core/mark-post-as-read (token access_token) post))

(defn get-mentions [access_token & options]
  "scope: my"
  (apply core/get-mentions (conj options (token access_token))))

(defn mark-mention-as-read [access_token mention]
  "scope: my"
  (core/mark-mention-as-read (token access_token) mention))