(ns app
  (:require ["pixi.js" :refer [Application AnimatedSprite Assets Texture Rectangle]]))

(defn ^:async create-app []
  (let [app (Application.)]
    (js-await (app.init {:background "#0f172b" :resizeTo js/window}))
    (.appendChild js/document.body app.canvas)
    app))

(defn ^:async create-sprite
  [url {:keys [frame-width frame-height cols rows]}]
  (let [texture (js-await (Assets.load url))
        area    (* cols rows)
        frames  (->> (range 0 area)
                     (mapv (fn [i]
                             (let [row (Math/floor (/ i cols))
                                   col (mod i cols)
                                   x (* col frame-width)
                                   y (* row frame-height)
                                   rect (Rectangle. x y frame-width frame-height)]
                               (Texture. {:source texture.source :frame rect})))))]
    (AnimatedSprite. frames)))

(defn ^:async run []
  (let [app       (js-await (create-app))
        astronaut (js-await (create-sprite "/astronaut.png" 
                                         {:frame-width 341 
                                          :frame-height 341
                                          :rows 3
                                          :cols 3}))
        stage     (.-stage app)
        screen    (.-screen app)]
    (set! (.-x astronaut) (/ screen.width 2))
    (set! (.-y astronaut) (/ screen.height 2))
    (.set astronaut.anchor 0.5)
    (set! (.-animationSpeed astronaut) 0.2)
    (astronaut.play)
    (stage.addChild astronaut)))

(run)
