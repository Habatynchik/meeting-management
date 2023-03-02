FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} meeting-management.jar
ENTRYPOINT ["java","-jar","/meeting-management.jar"]