FROM openjdk:11
ARG PATH=src/main/resources/docker/wait-for-it
COPY ${PATH}/wait-for-it.sh ${PATH}/entrypoint.sh ./
ENV LANG C.UTF-8
EXPOSE 8080
ARG JAR_FILE=target/battleship-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["./entrypoint.sh"]
CMD ["java", "-jar", "app.jar", "-Djava.net.preferIPv4Stack=true"]

# in battleship folder
# docker build -f src/main/resources/docker/Dockerfile -t feczkob/battleship:battleship-1.0 ./
# docker build -f src/main/resources/docker/Dockerfile -t battleship:1.0 ./
# push: https://jsta.github.io/r-docker-tutorial/04-Dockerhub.html