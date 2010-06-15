(ns org.yogthos.mandelbrot
  (:import (java.awt Color Container Graphics Canvas Dimension) 
           (javax.swing JPanel JFrame) 
           (java.awt.image BufferedImage BufferStrategy))) 

;(set! *warn-on-reflection* true) 

(let [width          (float 640)
       height        (float 640)
       max-steps     (float 32)
       color-scale   (float (quot 255 max-steps))
       height-factor (/ 2.5 height)
       width-factor  (/ 2.5 width)]

   (defn on-thread [#^Runnable f] (.start (new Thread f)))

   (defn check-bounds [x y]
     (let [f2 (float 2.0)
           f4 (float 4.0)]
       
       (loop [px    (float x)
              py    (float y)
              zx    (float 0.0)
              zy    (float 0.0)
              zx2   (float 0.0)
              zy2   (float 0.0)
              value (float 0.0)]
         
         (if (and (< value max-steps) (< (+ zx2 zy2) f4))
           (let [new-zy  (float  (+ (* (* f2 zx) zy) py))
                 new-zx  (float  (+ (- zx2 zy2) px))
                 new-zx2 (float (* new-zx new-zx))
                 new-zy2 (float (* new-zy new-zy))]
             (recur px py new-zx new-zy new-zx2 new-zy2 (inc value)))
           (if (== value max-steps) 0 value)))))

  (defn draw-line [#^Graphics g y]
    (let [dy (float (- 1.25 (* y height-factor)))]
      (doseq [x (range 0 width)]
        (let [dx (float (- (* x width-factor) (float 2.0)))]
          (let [value   (check-bounds dx dy)
                scaled  (Math/round (float (* value color-scale)))
                xscaled (Math/round (float (* x (/ 255 width))))]
            (if (> value  0)
              (.setColor g
                  (new Color 255 (- 255 scaled) scaled))
              (.setColor g 
                  (new Color xscaled (- 255 xscaled) xscaled)))
            (.drawRect g  x y 0 0)))))) 

   (defn draw-lines
     ([buffer g] (draw-lines buffer g height))
     ([#^BufferStrategy buffer g y]
        (doseq [y (range 0 y)]                    
          (on-thread (draw-line g y))
          (.show buffer))))

   (defn draw [#^Canvas canvas]
     (let [buffer (.getBufferStrategy canvas)
           g      (.getDrawGraphics buffer)]
       (draw-lines buffer g)))

   (defn main []

     (let [panel  (new JPanel)
           canvas (new Canvas)
           frame  (new JFrame "Mandelbrot")]

       (doto panel
         (.setPreferredSize (new Dimension width height))
         (.setLayout nil)
         (.add canvas))

       (doto frame
         (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
         (.setBounds 0 0 width height)
         (.setResizable false)
         (.add panel)
         (.setVisible true))

       (doto canvas
         (.setBounds 0 0 width height)
         (.setBackground (Color/BLACK))
         (.createBufferStrategy 2)
         (.requestFocus))

       (draw canvas)))) 

(time (main))


