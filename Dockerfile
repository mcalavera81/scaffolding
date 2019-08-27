FROM maven:3.6.1-jdk-8-slim AS build
ENV APP_HOME=/app
WORKDIR $APP_HOME
COPY src $APP_HOME/src
COPY pom.xml $APP_HOME
RUN mvn clean package

FROM openjdk:8-jre-slim
ENV APP_HOME=/app/
WORKDIR $APP_HOME
COPY --from=build /app/target/exercise-wallet-0.0.1-SNAPSHOT.jar $APP_HOME/exercise-wallet-0.0.1-SNAPSHOT.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","exercise-wallet-0.0.1-SNAPSHOT.jar"]