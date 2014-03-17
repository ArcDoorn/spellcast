(ns spellcast.model.player
  (:use clojure.tools.logging))

(defn add-player
  "add a player with the given name and communication channel to the given world"
  [world name channel]
  (when (not (:started world))
    (info (str "player " name " world added"))
    (assoc world :players
           (assoc (:players world)
             name {:ready false
                   :alive true
                   :active true}))))

(defn get-players
  "Return the list of player with name included."
  [world]
  (for [[name player]
        (:players (world))]
    (assoc player :name name)))
