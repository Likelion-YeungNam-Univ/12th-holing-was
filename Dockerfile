FROM openjdk:17-alpine

CMD ["java","-jar","/build/libs/holing-0.0.1-SNAPSHOT.jar"]

COPY ./build/libs/holing-0.0.1-SNAPSHOT.jar /build/libs/holing-0.0.1-SNAPSHOT.jar