# how to build


mvn package


# how to run

1) modify application.properties file,
   change of Core.Data, and Markov data file location
   Make sure markov data file folder exsits

2) change port accordingly, some port on your machine maybe used.
   for each microserice

3) config each microservice endpoint under web application.property file

4) run each microservice seperately in its own terminal, last one you start should be web project.

java -jar .\syllable\target\syllable-0.0.1-SNAPSHOT.jar will start syllable service
java -jar .\rhyme\target\rhymer-0.0.1-SNAPSHOT.jar will start rhyme service
java -jar .\markov\target\markov-0.0.1-SNAPSHOT.jar will start markov service
java -jar web/target/web-1.0.jar will start the web app