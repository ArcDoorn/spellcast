(ns spellcast.control.game-control
  (:require
   [clojure.core :as core]
   [clojure.core.async :as async :refer [chan <!! >!! go <! >!]])
    (:use
     clojure.tools.logging
     spellcast.model.game-logic
     spellcast.control.universe
     spellcast.control.player
     spellcast.model.player))

(defn publish-result
  "Publish the result of the current game step to the players."
  [world]
  (println world)) ; FIXME

(defn wait-until
  "returns a response channel, that waits until the test is
  fullfilled after reading a message from the input channel."
  [channel test?]
  (go (while (and (<! channel)
                  (test?)))))

(defn game-started!!
  "waits until the game started."
  [world-name]
  (while (and
          (<!! (:chan (world world-name)))
          (not (:started (world world-name))))))

(defn all-players-ready!!  ;; here is a bug! use structure from above
  "Read from the world channels until all players are ready."
  [world]
  (let [players (get-players world),
        chan (:chan world)]
    (while (and (<!! chan)
                (not (every? :ready players))))))

(defn play-round!!
  "When all players are ready evaluate the spells."
  [world-name]
  (all-players-ready!! (world world-name))
  (update-world! world-name evaluate-spells)
  (publish-result (world world-name)))

(defn game-loop!! ;; FIXME correct loop
  "Main game loop runs until the game is over."
  [world-name]
  (game-started!! world-name)
  (while (not (game-over? (world world-name)))
    (play-round!! world-name))
  (remove-world! world-name))

(defn new-game!
  "Start the game in a new world"
  [world-name]
  (create-world! world-name)
  (future (game-loop!! world-name)))
