(ns org.yogthos.mandelbrot
  (:import (java.awt Color Container Graphics Canvas Dimension) 
           (javax.swing JPanel JFrame) 
           (java.awt.image BufferedImage BufferStrategy))) 

;(set! *warn-on-reflection* true) 

(let [width (float 640)
       height (float 640)
       max-steps (float 32)
       color-scale (float (quot 255 max-steps))
       height-factor (/ 2.5 height)
       width-factor (/ 2.5 width)]

   (defn on-thread [#^Runnable f] (doto (new Thread f) (.start)))

   (defn check-bounds [x y]
     (let [f2 (float 2.0)
           f4 (float 4.0)]
       (loop [px (float x)
              py (float y)
              zx (float 0.0)
              zy (float 0.0)
              zx2 (float 0.0)
              zy2 (float 0.0)
              value (float 0)]
         (if (and (< value max-steps) (< (+ zx2 zy2) f4))
           (let [new-zy (float (+ (* (* f2 zx) zy) py))
                 new-zx (float (+ (- zx2 zy2) px))
                 new-zx2 (float (* new-zx new-zx))
                 new-zy2 (float (* new-zy new-zy))]
             (recur px py new-zx new-zy new-zx2 new-zy2 (inc
                                                         value)))
           (if (== value max-steps) 0 value)))))

  (defn draw-line [#^Graphics g y]
    (let [dy (- 1.25 (* y height-factor))]
      (doseq [x (range 0 width)]
        (let [dx (- (* x width-factor) 2.0)]
          (let [value (check-bounds dx dy)
                scaled (Math/round (* value color-scale))
                xscaled (Math/round (* x (/ 255 width)))]
            (if (> value  0)
              (. g setColor
                  (Color.  255 (- 255 scaled) scaled))
              (. g setColor
                  (Color. xscaled (- 255 xscaled) xscaled)))
            (. g drawRect x y 0 0)))))) 

   (defn draw-line-old [#^Graphics g y]
     (let [dy (- 1.25 (* y height-factor))]
       (doseq [x (range 0 width)]
         (let [dx (- (* x width-factor) 2.0)]
           (let [value (check-bounds dx dy)]
             (if (> value  0)
               (doto g
                (. setColor (let [scaled (Math/round (* value color-scale))]
                     (Color.   255 (mod 255 scaled) scaled))) 
                ;(. setColor (Color. (* value color-scale)))
                 (. drawRect x y 0 0))))))))

   (defn draw-lines
     ([buffer g] (draw-lines buffer g height))
     ([#^BufferStrategy buffer g y]
        (doseq [y (range 0 y)]
          ;(draw-line g y)
          (on-thread (draw-line g y))
          (. buffer show))))

   (defn draw [#^Canvas canvas]
     (let [buffer (. canvas getBufferStrategy)
           g        (. buffer getDrawGraphics)]
       (draw-lines buffer g)))

   (defn main []

     (let [panel (JPanel.)
           canvas (Canvas.)
           frame (JFrame. "Mandelbrot")]

       (doto panel
         (.setPreferredSize (Dimension. width height))
         (.setLayout nil)
         (.add canvas))

       (doto frame
         (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
         (.setBounds 0,0,width height)
         (.setResizable false)
         (.add panel)
         (.setVisible true))

       (doto canvas
         (.setBounds 0,0,width height)
         (.setBackground (Color/BLACK))
         (.createBufferStrategy 2)
         (.requestFocus))

       (draw canvas)))) 
(time (main))

