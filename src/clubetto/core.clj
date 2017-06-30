(ns clubetto.core)

(def PI Math/PI)
(def HALF_PI (/ Math/PI 2))

(def initial-rad-by-dir
  {:north PI,
   :west  HALF_PI,
   :south 0,
   :east  (+ PI HALF_PI)})

(def DEFAULT_FPS 30)
(def DEFAULT_ANIM_DURATION 2)
(def DEFAULT_SZ 100)

;
; (fps:frame/seconds) * (duree animation:second) = (nb frame for anim)
;
(defn- nb-frame [fps duration]
  (* fps duration))

(defn- printable-context [context]
  (assoc context :anim-fns nil))

(defn new-context [config program]
  (let [sz (get config :sz DEFAULT_SZ)
        animDuration (get config :anim-duration-seconds DEFAULT_ANIM_DURATION)
        fps (get config :fps DEFAULT_FPS)
        x (get config :cx 1)
        y (get config :cy 1)
        direction (get config :direction :south)
        nbFrameForAnimation (nb-frame fps animDuration)
        context {:sz             sz
                 :fps            fps
                 :anim-nb-frame  nbFrameForAnimation
                 :anim-mov-delta (/ sz nbFrameForAnimation)
                 :anim-rot-delta (/ HALF_PI nbFrameForAnimation)
                 :x              (* (inc x) sz)
                 :y              (* (inc y) sz)
                 :angle          (get initial-rad-by-dir direction)
                 :anim           nbFrameForAnimation
                 :anim-fn        identity
                 :anim-fns       program}]
    (println "Initial context" (printable-context context))
    context))

;;

(defn turn-left [state]
  (let [ang (:angle state)
        delta (:anim-rot-delta state)]
    (-> state
        (assoc :angle (- ang delta)))))

(defn turn-right [state]
  (let [ang (:angle state)
        delta (:anim-rot-delta state)]
    (-> state
        (assoc :angle (+ ang delta)))))

(defn move [state]
  (let [x (:x state)
        y (:y state)
        r (:angle state)
        d (:anim-mov-delta state)
        dx (* d (Math/sin r))
        dy (* d (Math/cos r))]
    (-> state
        (assoc :x (- x dx))
        (assoc :y (+ y dy)))))

(defn update-state [state]
  (let [anim (:anim state)
        anim-max (:anim-nb-frame state)]
    (if (< anim anim-max)
      (let [afn (:anim-fn state)]
        (-> (afn state)
            (assoc :anim (inc anim))))
      (let [fns (:anim-fns state)
            nfn (first fns)
            nfnf (if (nil? nfn) identity nfn)
            nfns (rest fns)]
        (-> state
            (assoc :anim-fn nfnf)
            (assoc :anim-fns nfns)
            (assoc :anim 0))))))
