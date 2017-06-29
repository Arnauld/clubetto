(ns clubetto.draw
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [clubetto.core :as c]))

(defn draw-state [state]
  ; Clear the sketch by filling it with light-grey color.
  (q/background 240)
  (let [sz (:sz state)
        hsz (/ sz 2)]
    ; Move origin point to the center of the cubetto.
    (q/with-translation
      [(- (:x state) hsz) (- (:y state) hsz)]
      (q/with-rotation
        [(:angle state)]
        ; Draw the cubetto
        (q/fill 83 160 219)
        (q/rect (- hsz) (- hsz) sz sz)
        (q/fill 0)
        (q/ellipse -10 (- hsz 10) 7 7)
        (q/ellipse 10 (- hsz 10) 7 7)))))



