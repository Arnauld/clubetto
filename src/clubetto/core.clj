(ns clubetto.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(def PI Math/PI)
(def HALF_PI (/ Math/PI 2))
(def TWO_PI (* Math/PI 2))

(def N PI)
(def W HALF_PI)
(def S 0)
(def E (+ PI HALF_PI))


(def FPS 30)
(def ANIM_SEC 2)
(def SZ 100)
;
; (fps:frame/seconds) * (duree animation:second) = (nb frame for anim)
;
(def NB_FRAME_PER_ANIM (* FPS ANIM_SEC))
(def ROT_PER_FRAME (/ HALF_PI NB_FRAME_PER_ANIM))
(def MOV_PER_FRAME (/ SZ NB_FRAME_PER_ANIM))

;;



;;

(defn clamp-angle [angle]
  (if (< TWO_PI angle)
    (- angle TWO_PI)
    (if (> 0 angle)
      (+ angle TWO_PI)
      angle)))

(defn turn-left [state]
  (let [r (:rot state)]
    (-> state
        (assoc :rot (clamp-angle (- r ROT_PER_FRAME))))))

(defn turn-right [state]
  (let [r (:rot state)]
    (-> state
        (assoc :rot (clamp-angle (+ r ROT_PER_FRAME))))))

(defn close-to? [n d]
  (< (Math/abs ^double (- n d)) 1e-3))

(defn angle-close-to? [n d]
  (or (close-to? n d)
      (close-to? (- n TWO_PI) d)
      (close-to? n (- d TWO_PI))))

(defn move [state]
  (let [x (:x state)
        y (:y state)
        r (:rot state)]
    (cond
      (angle-close-to? r N) (-> state
                                (assoc :y (- y MOV_PER_FRAME)))
      (angle-close-to? r E) (-> state
                                (assoc :x (+ x MOV_PER_FRAME)))
      (angle-close-to? r S) (-> state
                                (assoc :y (+ y MOV_PER_FRAME)))
      (angle-close-to? r W) (-> state
                                (assoc :x (- x MOV_PER_FRAME))))))

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate FPS)
  ; setup function returns initial state
  {:x        SZ
   :y        SZ
   :rot      S
   :anim     1
   :anim-fn  identity
   :anim-fns [move, turn-left, move, turn-right, move, move]})

(defn update-state [state]
  (let [anim (:anim state)]
    (if (< anim NB_FRAME_PER_ANIM)
      (let [afn (:anim-fn state)]
        (-> (afn state)
            (assoc :anim (inc anim))))
      (let [fns (:anim-fns state)
            nfn (first fns)
            nfnf (if (nil? nfn) identity nfn)
            nfns (rest fns)
            nst (-> state
                    (assoc :anim-fn nfnf)
                    (assoc :anim-fns nfns)
                    (assoc :anim 0))]
        (println "state changed" nst)
        nst))))

(defn draw-state [state]
  ; Clear the sketch by filling it with light-grey color.
  (q/background 240)
  (let [hsz (/ SZ 2)]
    ; Move origin point to the center of the sketch.
    (q/with-translation
      [(- (:x state) hsz) (- (:y state) hsz)]
      (q/with-rotation
        [(:rot state)]
        ; Draw the cubetto
        (q/fill 83 160 219)
        (q/rect (- hsz) (- hsz) SZ SZ)
        (q/fill 0)
        (q/ellipse -10 (- hsz 10) 7 7)
        (q/ellipse 10 (- hsz 10) 7 7)))))

(q/defsketch
  clubetto
  :title "You spin my circle right round"
  :size [500 500]
  ; setup function called only once, during sketch initialization.
  :setup setup
  ; update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])
