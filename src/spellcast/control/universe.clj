(ns spellcast.control.universe
  (:use clojure.tools.logging)
  (:require
   [clojure.core :as core]
   [clojure.core.async :as async :refer [chan <!! >!! go <! >!]]))

(def universe
  "The central data for all worlds."
  (atom {}))

(defn init!
  "We start with an empty universe, use this in repl to restart."
  []
  (info "Reset universe to initial state")
  (reset! universe {}))

(defn create-world!
  "Add a new world to the universe."
  [name]
  (info (str "world " name " created"))
  (swap! universe
         (fn create-world [worlds]
           (assoc worlds
             name {:players {},
                   :started false,
                   :chan (chan)}))))

(defn remove-world!
  "removes the given world from the univers"
  [world-name]
  (info (str "world " world-name " removed."))
  (swap!
   universe
   (fn remove-world [worlds]
     (dissoc worlds world-name))))

(defn world
  "Get a specific world from the universe."
  [name]
  (get @universe name))


(defn update-world!
  [world-name world-updater]
  (swap! universe
         (fn update-world [worlds]
           (let [world (get worlds world-name)]
             (assoc worlds
               world-name (world-updater world)))))
  (go
   (>! (:chan (world world-name))
       :update))) 

