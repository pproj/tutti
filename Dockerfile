FROM amazoncorretto:11-alpine

ARG APPLICATION_USER=tutti
RUN adduser --no-create-home -u 1000 -D $APPLICATION_USER

# Configure working directory
RUN mkdir /app && \
    chown -R $APPLICATION_USER /app

USER 1000

COPY --chown=1000:1000 ./build/libs/*-standalone.jar /app/tutti.jar
WORKDIR /app

ENTRYPOINT [ "java", "-jar", "/app/tutti.jar" ]
