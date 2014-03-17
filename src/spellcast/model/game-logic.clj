(ns spellcast.model.game-logic
  (:use clojure.tools.logging))

(defn evaluate-spells ;; FIXME: put some logic here
  [world]
  world)

(defn game-over?  
  "Returns true if the game for the given world is finished."
  [world]
  (info (str "game over?!"))
  (<= (count
       (filter (fn [player]
                 (info (str player
                             (:active player)
                             (:alive player)))
                 (and (:active player)
                      (:alive player)))
               (:players world)))
      1))
