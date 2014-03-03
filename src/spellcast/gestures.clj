(ns spellcast.gestures)
; :F :P :S :W :D :df :dp :ds :dw :dd :C :stab nil


(defn replace-double-gesture
  "Resolve douple gesture keywords by vectors.
   Double gestures are :df :dp :ds :dw :dd :C,
   where the clap :C has no resolution to single gesture
   (What is the sound of claping single-handed?).
   Other gestures will be returned unchanged."
  [gestures]
  (cond (= gestures :df) [:F :F]
        (= gestures :dp) [:P :P]
        (= gestures :ds) [:S :S]
        (= gestures :dw) [:W :W]
        (= gestures :dd) [:D :D]
        (= gestures :C) [nil nil]
        :else gestures))

(defn left
  "Returns the left-hand gesture from the two-hand gesture pair."
  [gestures]
  (first
   (replace-double-gesture gestures)))

(defn right
  "Returns the right-hand gesture from the two-hand gesture pair."
    [gestures]
  (second
   (replace-double-gesture gestures)))

(defn match-gesture [spell-guesture hand-guestures side]
  (or
   (= spell-guesture hand-guestures)
   (= spell-guesture (side hand-guestures))))

(defn match-spell [spell gestures side]
  (every? identity
          (map (fn check-guesture [spell-gesture hand-gestures]
                 (match-gesture spell-gesture hand-gestures side))
               spell gestures)))

(defn find-spells [spell-book gestures side]
  (filter (fn check-spell [[spell-gestures _]]
            (match-spell (reverse spell-gestures)
                         gestures
                         side))
          spell-book))

