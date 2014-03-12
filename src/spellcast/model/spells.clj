(ns spellcast.model.spells)

(defmacro defspell
  "Defines a spell.
   A spell needs a name, attributes (priority),
   a description, arguments and its body.
   A general spell expects the world as first, the owner as second
   and perhaps a target as third argument.
   The spell should return the modified world."
  [name attributes description arguments & body]
  `(def ~name ~(assoc attributes
                 :needs-target (>= 3 (count arguments))
                 :description description
                 :call `(fn ~arguments ~@body))))

(defspell magic-missle
  {:priority 3}
  "Hits someone with a ball of pure energy."
  [world owner target]
  (println "Not yet implemented."))