FROM openjdk:17
RUN echo "TESTING WASSUP"
ADD target/megatransact-backend.jar megatransact-backend.jar
ENTRYPOINT ["java", "-jar", "/megatransact-backend.jar"]