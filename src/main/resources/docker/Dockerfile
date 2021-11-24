FROM openjdk:11
ENV LANG C.UTF-8
EXPOSE 8080
ARG JAR_FILE=target/battleship-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "-Djava.net.preferIPv4Stack=true"]

# in battleship folder
#  docker build -f src/main/resources/docker/Dockerfile -t feczkob/battleship:battleship-1.0 ./