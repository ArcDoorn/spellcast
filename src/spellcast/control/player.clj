(ns spellcast.control.player
  (:use clojure.tools.logging
        spellcast.control.universe)
  (:require
   [clojure.core :as core]
   [clojure.core.async :as async :refer [chan <!! >!! go <! >!]]))

(defn report-to-player!!
  "Create a thread, that reads the communication channel and send
  messages to the player"
  [player-name channel]
  (go
   (while true
     (let [message (<! channel)]
       (info (str "@" player-name " " message))))))

(defn add-player!! 
  "Add a player to the given world and establish a reporting
  connection."
  [world-name player-name] 
  (let [channel (chan)]
    (update-world
     world-name
     (fn player-to-world
       [world]
       (add-player world name channel))) 
    (report-to-player!! player-name channel)))

  