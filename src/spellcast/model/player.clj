(ns spellcast.model.player)

(defn add-player
  "add a player with the given name and communication channel to the given world"
  [world name channel]
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
