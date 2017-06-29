(ns clubetto.draw
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [clubetto.core :as c]))

(defn draw-cubetto [state]
  (let [sz (:sz state)
        hsz (/ sz 2)]
    ; Move origin point to the center of the cubetto.
    (q/with-translation
      [(- (:x state) hsz) (- (:y state) hsz)]
      (q/with-rotation
        [(:angle state)]
        ; Draw the cubetto
        (q/stroke-weight 1)
        (q/stroke 0)
        (q/fill 83 160 219)
        (q/rect (- hsz) (- hsz) sz sz)
        (q/fill 0)
        (q/ellipse -10 (- hsz 10) 7 7)
        (q/ellipse 10 (- hsz 10) 7 7)))))

(defn draw-axis [state]
  (let [sz (:sz state)]
    (q/stroke-weight 2)
    (q/stroke 255 0 0)
    (q/line 10 10 sz 10)
    (q/text "x" sz 10)
    (q/stroke 0 255 0)
    (q/line 10 10 10 sz)
    (q/text "y" 10 sz)))

(defn draw-state [state]
  ; Clear the sketch by filling it with light-grey color.
  (q/background 240)
  (draw-axis state)
  (draw-cubetto state))



