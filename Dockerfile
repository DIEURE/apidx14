# 1. Compilação
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

RUN apk add --no-cache dos2unix

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw

COPY src ./src

# Normaliza finais de linha e remove caracteres corrompidos
RUN find src/main/resources -type f -name "*.properties" -exec dos2unix {} +

# Executa o build forçando UTF-8 na JVM
RUN ./mvnw clean package -DskipTests -Dfile.encoding=UTF-8

# 2. Execução leve
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

COPY --from=builder /app/target/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod
ENV TZ=America/Sao_Paulo

EXPOSE 8080

ENTRYPOINT ["java", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-Xms192m", \
  "-Xmx300m", \
  "-XX:MaxMetaspaceSize=128m", \
  "-XX:+UseContainerSupport", \
  "-XX:+TieredCompilation", \
  "-XX:TieredStopAtLevel=1", \
  "-jar", "app.jar"]
