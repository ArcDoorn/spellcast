(ns spellcast.core
  (:require
   [clojure.core :as core]
   [clojure.core.async :as async :refer [chan <!! >!! go <! >!]])
  (:use clojure.tools.logging
        spellcast.control.universe
        spellcast.control.player
        spellcast.control.game-control
        spellcast.model.player
        spellcast.model.game-logic))

;; (defn test-game [world players]
;;   (new-game! world)
;;   (for [player players]
;;     (add-player!! world player))
;;   (start-game! world))

;; (defn test-world []
;;   (->
;;    (new-world)
;;    (add-player :foo (chan))
;;    (add-player :bar (chan))))

;; (game-over? (test-world))

(defn test-game []
  (new-game! :myworld)
  (add-player!! :myworld :foo)
  (add-player!! :myworld :bar)
 ; (start-game! :myworld)
  )