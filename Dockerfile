FROM alpine/git as clone
ENV APPROOT="/app"
WORKDIR $APPROOT
RUN git clone https://github.com/navkkrnair/10-2019-search-service.git

FROM maven:3.6-jdk-8-alpine as build
ENV APPROOT="/app"
WORKDIR $APPROOT
COPY --from=clone $APPROOT/10-2019-search-service $APPROOT
RUN mvn package


FROM openjdk:8-jre-alpine
MAINTAINER "navkkrnair@gmail.com"
ENV APPROOT="/app"
WORKDIR $APPROOT 
COPY --from=build $APPROOT/target/search-service-1.0.jar $APPROOT
EXPOSE 8090
ENTRYPOINT ["java"]
CMD ["-jar","-Xmx512m","-Xms512m","-Djava.security.egd=file:/dev/./urandom", "search-service-1.0.jar"]