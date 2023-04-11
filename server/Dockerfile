FROM amazoncorretto:11
COPY target/*.jar server.jar
ENTRYPOINT ["java","-jar","-Dserver.port=9090","/server.jar"]