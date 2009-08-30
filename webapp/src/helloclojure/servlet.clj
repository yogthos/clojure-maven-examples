(ns helloclojure.servlet           
	(:require (org.danlarkin [json :as json]))                                                             
  (:gen-class :extends javax.servlet.http.HttpServlet))             
                                                                                                                              
(defn -doGet                                                  
  [_ request response]     
  (.setContentType response "text/html")                                                                                                   
  (let [json-req (json/decode-from-str (.getParameter request "Name"))
  			w        (.getWriter response)]                                                                 
    (.println w 
			(str "<html>"
			"<head>" 
			 "<title>Hello World!</title>"
			"</head>" 
			 "<body>" 
			"<h1>Hello "
			(:name json-req) 
			;(.getParameter request "Name")
			"</h1>" 
			"</body>" 
			"</html>")))) 
			
(defn -doPost [_ request response]
	(-doGet nil request response))			