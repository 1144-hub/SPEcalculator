FROM eclipse-temurin:24-jre

WORKDIR /app


COPY target/ScientificCalculator-1.0-SNAPSHOT.jar /app/calculator.jar


ENTRYPOINT ["java", "-jar", "calculator.jar"]