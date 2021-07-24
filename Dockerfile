#
# Build stage
#
FROM openjdk:11-jdk-slim
#VOLUME /tmp
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} webchat-server.jar
EXPOSE 8089
ENTRYPOINT ["java","-jar","/webchat-server.jar"]
# to build and run
#docker build -t webchat-server .
#docker run -p 8099:8099 rebounder-chain-backend