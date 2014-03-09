(ns spellcast.core
  (:use clojure.tools.logging)
  (:require [clojure.core.async :as async :refer :all]))

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
  (swap! universe
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

(defn add-player
  [world name]
  (info (str "player " name " world added"))
  (assoc world :players
         (assoc (:players world)
           name {:ready false,
                 :active true}))) 

(defn get-players
  "Return the list of player with name included."
  [world]
  (for [[name player]
        (:players (world))]
    (assoc player :name name)))


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

(defn all-players-ready!!
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

(defn game-loop!!
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
