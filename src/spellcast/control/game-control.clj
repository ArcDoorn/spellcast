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

(defmacro wait-until!  
  "Waits until the test is fullfilled after reading a message from the
  input channel. Must be used in a go block."
  [channel & test?]
  `(while (and (<! ~channel)
               (not ~@test?))))

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
  (go ; should be replayed by a wait until 
      (all-players-ready!! (world world-name))
      ; evaluate guestures
      ; tell possible spells to player
      ; wait for spell selection and target
      (update-world! world-name evaluate-spells)
      ; --> this should be done step by step
      ; --> each step should be directly published
      ))

(defn game-loop!! 
  "Main game loop runs until the game is over."
  [world-name]
  (let [ch (:chan (world world-name))]
    (go
     (wait-until! ch (:started (world world-name)))
     (while (not (game-over? (world world-name)))
       (<! (play-round!! world-name)))
     (remove-world! world-name))))

(defn new-game!
  "Start the game in a new world"
  [world-name]
  (create-world! world-name)
  (go (<! (game-loop!! world-name))))


