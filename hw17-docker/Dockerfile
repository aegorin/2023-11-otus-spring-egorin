FROM bellsoft/liberica-openjdk-alpine:17.0.11 as builder
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN --mount=type=cache,target=/root/.m2 ./mvnw install -DskipTests
RUN cd target && \
    java -Djarmode=layertools -jar application-hw17-docker.jar extract --destination extracted-layers

FROM bellsoft/liberica-openjre-alpine:17.0.11
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/extracted-layers
RUN addgroup -S library-appgroup && adduser -S library-appusr -G library-appgroup
USER library-appusr:library-appgroup

WORKDIR /application
COPY --from=builder ${DEPENDENCY}/dependencies .
COPY --from=builder ${DEPENDENCY}/spring-boot-loader .
COPY --from=builder ${DEPENDENCY}/snapshot-dependencies .
COPY --from=builder ${DEPENDENCY}/application .
EXPOSE 8080/tcp
ENTRYPOINT ["sh", "-c",  "java ${JAVA_OPTS} org.springframework.boot.loader.launch.JarLauncher ${@}"]
