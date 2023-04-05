FROM amazoncorretto:11
COPY target/*.jar gateway.jar
ENTRYPOINT ["java","-jar", "-Dserver.port=8080", "/gateway.jar"]