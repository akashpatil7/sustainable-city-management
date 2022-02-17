FROM scmbackend.azurecr.io/externaldata:v1

EXPOSE 8080

ADD ./target/*.jar app.jar

WORKDIR /

ENTRYPOINT ["java","-Djdk.tls.client.protocols=TLSv1.2","-jar","/app.jar"]
