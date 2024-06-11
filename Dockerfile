FROM openjdk:17
ADD target/megatransact-backend.jar megatransact-backend.jar
ENTRYPOINT ["java", "-jar", "/megatransact-backend.jar"]