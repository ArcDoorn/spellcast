(ns spellcast.core
  (:require [clojure.core.async :as async :refer :all]))

(def universe (atom {}))

(defn init! []
  (reset! universe {}))

(defn create-world! [name]
  (swap! universe
         (fn create-world [worlds]
           (assoc worlds
             name {:players {},
                   :started false,
                   :chan (chan)}))))

(defn world [name]
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
  [world]
  (while (and
          (<!! (:chan (world world-name)))
          (not (:started (world world-name))))

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

(defn game-loop!!
  "Main game loop runs until the game is over."
  [world-name]
  (game-started!! (world world-name))
  (letfn [(game-over [] true)] ;; FIXME
    (while (not (game-over))
      (play-round!! world-name))))

