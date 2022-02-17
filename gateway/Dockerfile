FROM scmbackend.azurecr.io/gateway:v1

EXPOSE 8080

ADD ./target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
