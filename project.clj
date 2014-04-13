(defproject typetalk "0.1.0-SNAPSHOT"
  :description "Typetalk API"
  :url "http://github.com/takuji/typetalk-clj"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-http "0.9.0"]
                 [org.clojure/data.json "0.2.4"]]
  :plugins [[lein-environ "0.4.0"]
            [lein-kibit "0.0.8"]]
  :profiles {:dev {:dependencies [[environ "0.4.0"]]}})
