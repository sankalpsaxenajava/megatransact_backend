# STAGE: BUILD
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /opt/app
COPY pom.xml /opt/app/
RUN mvn clean install -DskipTests > /dev/null 2>&1 || true
COPY . .
RUN mvn clean install -DskipTests
RUN ls -al /opt/app/target

# ################################################################################
# STAGE: SERVE
FROM openjdk:17 AS runner
COPY --from=builder /opt/app/target/megatransact-backend.jar /app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
