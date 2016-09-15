(ns draw.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(println "Some text at the console.")

(def pi 3.141592653589793)
(def twopi (* pi 2))
(defn pin [n] (/ pi n))
(defn sin [x] (. js/Math (sin x)))
(defn cos [x] (. js/Math (cos x)))
(defn atan2 [y x] (. js/Math (atan2 y x)))
(defn sqrt [x] (. js/Math (sqrt x)))
(defn random [x] (* (. js/Math (random)) x))
(defn rotate [[x y] alpha]
  "rotate a point around the origin by angle in radians"
  [(- (* x (cos alpha)) (* y (sin alpha)))
   (+ (* x (sin alpha)) (* y (cos alpha)))])
(defn translate [[x y] [xd yd]]
  "translate a point by another point"
  [(+ x xd) (+ y yd)])
(defn scale [[x y] s]
  [(* x s) (* y s)])
(defn deg-to-rad [deg]
  "convert an angle in degrees into radians"
  (* pi (/ deg 180)))
(defn rad-to-deg [rad]
  "convert an angle in radians to degrees"
  (* 180 (/ rad pi)))
(defn point-on-circle [alpha]
  "get a point on a circle at a given alpha angle"
  [(cos alpha) (sin alpha)])

(defn scale-translate [pts s p]
  "scale and translate a bunch of points by scale s and point p"
  (map #(translate % p)
       (map #(scale % s) pts)))

(defn printseq [s]
  "println the elements of a sequence, one per row"
  (doseq [x s] (println x)))

(defn polar-cart [a r]
  "convert a set of [angle radius] coords into [x y] coords"
  [(* r (cos a)) (* r (sin a))])

(defn randcircle [r cx cy numpoints]
  "produce a sequence of random points on a circle, anti-clockwise"
  (for [a (sort
           (for [i (range numpoints)] (random twopi)))]
    (translate (scale (polar-cart a r) r) [cx cy])))

;; from here on, impure functions

(defonce app-state (atom {:text "Hello world!"
                          :width 300
                          :height 300
                          :points []}))

(defn genpoints []
  (swap! app-state assoc :points
         (vec (flatten (randcircle 10 100 100 20)))))

(defn svg-polygon []
  "a representation for a regular polygon in svg and hiccup"
  [:svg
   {:width (:width @app-state)
    :height (:height @app-state)}
   [:g
    [:polygon {:points @app-state}
     :stroke "black"
     :stroke-width 1]]])

(defn root []
  ;[:h1 "abcd"]
  (svg-polygon))

(reagent/render-component [root]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
