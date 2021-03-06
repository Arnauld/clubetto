(ns clubetto.play
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [clubetto.core :as c]
            [clubetto.draw :as d]))

(def my-program (cycle [c/move,
                        c/turn-left,
                        c/move,
                        c/turn-right,
                        c/move,
                        c/move,
                        c/turn-left,
                        c/move,
                        c/turn-left,
                        c/move,
                        c/move,
                        c/turn-left,
                        c/move,
                        c/move, c/move,
                        c/turn-right,
                        c/move,
                        c/turn-right,
                        c/move,
                        c/turn-right]))
(def config {:cx 1, :cy 1, :anim-duration-seconds 1, :fps 10})



;
; ~~~ SETUP & START DISPLAY for QUIL
;

(def context (c/new-context config my-program))
(def sketch-sz (* 5 (:sz context)))

(defn setup []
  (q/frame-rate (:fps context))
  ; setup function returns initial state
  context)

(q/defsketch
  clubetto
  :title "Clubetto"
  :size [sketch-sz sketch-sz]
  ; setup function called only once, during sketch initialization.
  :setup setup
  ; update-state is called on each iteration before draw-state.
  :update c/update-state
  :draw d/draw-state
  :features [:keep-on-top]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])