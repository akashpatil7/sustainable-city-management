FROM openjdk:12.0.2

EXPOSE 8005

ADD ./target/*.jar app.jar

WORKDIR /

ENTRYPOINT ["java","-Djdk.tls.client.protocols=TLSv1.2","-jar","/app.jar"]
