FROM eclipse-temurin:11

LABEL mantainer="raman.mehta@fiftyfivetech.io"

WORKDIR /app

COPY target/springboot-restful-webservices-0.0.1-SNAPSHOT.jar /app/ums.jar

ENTRYPOINT ["java","-jar","ums.jar"]