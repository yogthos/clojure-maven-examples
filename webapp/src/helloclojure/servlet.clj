(ns helloclojure.servlet
	(:require [hiccup.page :refer [html5]])
  (:gen-class :extends javax.servlet.http.HttpServlet))

(defn -doGet
  [_ request response]
  (.setContentType response "text/html")
  (.. response
      getWriter
      (println
        (html5
          [:head [:title "Hello World"]]
          [:body [:h1 "Hello " (.getParameter request "Name")]]))))

(defn -doPost [this request response]
	(-doGet this request response))
