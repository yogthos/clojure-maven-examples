In order to setup a Clojure project with Maven 2, you need to follow these steps:

1. Get a copy of Maven installed (http://maven.apache.org/)
   On Ubuntu: apt-get install maven2

The first project we have is a sample Swing application that renders a Mandelbrot set.

The project can be run as follows:

```
mvn clean install && java -jar target/mandelbrot-example-1.0.jar
```

The second example is a simple web application based on the Servlet 2.5 specification, you can drop the resulting war on an Tomcat or Glassfish to see the result.

Hopefully, Both projects are fairly self explanatory and help you get started.
