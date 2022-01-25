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
   Run from main project directory - <b> cd gateway <b><br>
   <b>mvn clean package</b><br>
  <b>java -jar target/gateway-0.0.1-SNAPSHOT.jar</b><br>
  
Subsequently you can run any individual projects/microservices as required.
   
