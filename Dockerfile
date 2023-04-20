FROM amazoncorretto:11-alpine

COPY ./build/libs/*-standalone.jar /tutti.jar

ENTRYPOINT [ "java", "-jar", "/tutti.jar" ]
