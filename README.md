# sustainable-city-management
ASE project - 
To Run the project execute following commands in sequence - 
1. Run the eureka/discovery service - <br>
   Run from main project directory  - <b> cd eurekaserver </b><br>
   <b> mvn clean package </b> <br>
   <b> java -jar target/eurekaserver-0.0.1-SNAPSHOT.jar </b><br>
   
2. Run utility/library project - <br>
   Run from main project directory - <b> cd utils </b><br>
   <b> mvn clean install </b><br>

3. Run gateway service - <br>
   Run from main project directory - <b> cd gateway </b><br>
   <b>mvn clean package</b><br>
   <b>java -jar target/gateway-0.0.1-SNAPSHOT.jar</b><br>

4. Run user service - <br>
   Run from main project directory - <b> cd gateway </b><br>
   <b>mvn clean package</b><br>
   <b>java -jar target/user-service-0.0.1-SNAPSHOT.jar</b><br>

5. Run real-time-data-processor (run kafka/zookeeper commands below first)- <br>
   Run from main project directory - <b> cd gateway </b><br>
   <b>mvn clean package</b><br>
   <b>java -jar target/real-time-data-processor-0.0.1-SNAPSHOT.jar</b><br>

- For running the python microservice, inital setup is required first. 
- <br> Run from the main project directory - 
- <b> cd data-trends-predictions </b>
- <br> <b> python3 data_app.py </b>
   
```To Run the project with docker run following commands in a sequence - ```<br>
docker-compose build <br>
docker-compose up -d <br>

To Build only a particular service/container - <br> 
docker-compose up -d --no-deps --build <service_name><br>

To Stop all the containers -<br>
docker-compose stop<br>

To see logs of the service container - <br>
docker-compose logs <service_name>/<container_name> <br>

```Installing and Running Kafka & Zookeeper ```<br>
Links for installation: <br>
MAC: [https://hevodata.com/learn/install-kafka-on-mac/](url)<br>
Windows: [https://www.goavega.com/install-apache-kafka-on-windows/](url)<br>

Follow instructions in links to install and run zookeeper and kafka <br>
Install Scala 2.13 Binary in Kafka downloads page <br>

For example, on mac you navigate to the kafka directory (e.g. cd kafka_2.13-3.0.0) and run the following in separate terminals:

bin/zookeeper-server-start.sh config/zookeeper.properties

bin/kafka-server-start.sh config/server.properties

With Zookeeper and Kafka running the real-time-data-processor project should work

