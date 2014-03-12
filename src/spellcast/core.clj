(ns spellcast.core
  (:require
   [clojure.core :as core]
   [clojure.core.async :as async :refer [chan <!! >!! go <! >!]])
    (:use
     clojure.tools.logging
     spellcast.control.universe
     spellcast.control.player))

(defn evaluate-spells ;; FIXME: put some logic here
  [world]
  world)

(defn publish-result
  "Publish the result of the current game step to the players."
  [world]
  (println world)) ; FIXME

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

(defn game-over  ;; FIXME
  "Returns true if the game for the given world is finished."
  [world]
  true)

(defn game-loop!! ;; FIXME correct loop
  "Main game loop runs until the game is over."
  [world-name]
  (game-started!! world-name)
  (while (not (game-over (world world-name)))
    (play-round!! world-name))
  (remove-world! world-name))

(defn new-game!
  "Start the game in a new world"
  [world-name]
  (create-world! world-name)
  (future (game-loop!! world-name)))
