# Sustainable City Management
This application provides easy and intuitive real time visualisation of different data indicators in dublin city like dublin bikes, dublin bus , Pedestrian Data, Dublin AQI levels. It further uses complex predictive models to generate real time trends from this real time data. The trends within different data points are then combined together to generate real time recommendations based on the co-relation analysis within these data points. These recommendation will help the city managers/planners to efficeintly plan out city resources. In addition this application will also provvide the simulated data in case of failure or absesne/failure of any of the external data sources
This repository contains the backend services of this application. This application is divided into a set of individual microservice projects representing a unique service. Following are the set of microservices representing different services:</br>
</br>
<b> Gateway </b>- (gateway) Gateway service acts as an entry point for the application. All the external requests are routed to the individual microservices through the gateway. It's also used to perform security checks and filter out requests that fail to satisfy the security requirements.  authorization. It routes requests to individual services using a service address registry. </br>
<b> Eureka </b>- (eurekaserver) Acts as a service registry.</br> 
<b> User </b> - (user-service)  handles user registration and login.</br>
<b> Util </b>- (util) A simple library project having utility functions like generating tokens etc.</br>
<b> Real Time Data </b>- (real-time-data-processor) service  is responsible for fetching the real time data from external data providers. It also provides simulated data in case of failure of external data source.</br>
<b> Trends And Recommendations </b>- (data-trends-predictions) python service is used to provide real time trend analysis and recommendations. IT also implements simulation models to generate simulated data.</br>

### Prerequisites to run the project -
Java 8/8+ </br>
Docker https://docs.docker.com/get-docker/ </br>

To run the application locally run the following commands in sequence (N.B. TCD Wifi interferes with this so please be on another network)

1. Eureka (Discovery Service)
   <br> From the main project directory: `cd eurekaserver`
   <br>`mvn clean package`</br>
   
2. Run utility/library project - <br>
   Run from main project directory - `cd utils`
   <br> `mvn clean install`</br>

3. Run gateway service - <br>
   Run from main project directory - `cd gateway`<br>
   `mvn clean package`<br>

4. Run user service - <br>
   Run from main project directory - `cd user-service`<br>
   `mvn clean package`<br>

5. Run real-time-data-processor - <br>
   Run from main project directory - `cd gateway`<br>
   `mvn clean package`<br>

6. After running the above commands, Docker can be used to run the application on the local setup.  Run the following commands get application up and running (N.B. TCD Wifi    interferes with this so please be on another network)</br>
   `docker-compose build`</b></br>
   `docker-compose up -d`</b></br>
   

#### To Build only a particular service/container - <br> 
`docker-compose up -d --no-deps --build <service_name>`<br>

#### To Stop all the containers -<br>
`docker-compose stop`<br>

#### To see logs of the service container - <br>
`docker-compose logs <service_name>/<container_name>`
