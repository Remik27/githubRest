FROM eclipse-temurin:17
COPY build/libs/*.jar restApp.jar
ENTRYPOINT ["java","-jar","/restApp.jar"]