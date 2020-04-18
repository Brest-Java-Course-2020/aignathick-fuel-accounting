![Java CI](https://github.com/alexanderignathick/aignathick-fuel-accounting/workflows/Java%20CI/badge.svg)

# Fuel accounting
main course project

## How build
Setup java 11 and Maven, see [enviroment_setup.md](enviroment_setup.md)

## Build project 
Goto Project folder and execute  
    
    mvn clean install
    
## Postman requests collection
For testing rest queries, there is postman request collection available [postman requests collection](aignathick_fuel_acounting.postman_collection.json)

## Rest Server
### Start Rest using Maven Jetty plugin
```
mvn jetty:run -pl fuel-accounting-rest-app
```    
or
 ``` 
cd fuel-accounting-rest-app
mvn jetty:run
```
## Available Rest endpoints
### version
    curl --location --request GET 'localhost:8088/version'
### fuel dtos
```
curl --location --request GET 'localhost:8088/fuel_dtos'
```
Pretty print json:
```
curl --location --request GET 'localhost:8088/fuel_dtos' | json_pp       
```
### fuels

#### findAll
    curl --location --request GET 'localhost:8088/fuels' | json_pp

#### create
    curl --location --request POST 'localhost:8088/fuels/' \
    --header 'Content-Type: application/json' \
    --data-raw '{
        "fuelId": null,
        "fuelName": "PostmanFuel"
    }'
    
#### findById
    curl --location --request GET 'localhost:8088/fuel/1'| json_pp

#### update
    curl --location --request PUT 'localhost:8088/fuels/' \
    --header 'Content-Type: application/json' \
    --data-raw '{
        "fuelId": 1,
        "fuelName": "AI95"
    }'

#### delete
    curl --location --request DELETE 'localhost:8088/fuels/3'
    
### transports

#### findAll
    curl --location --request GET 'localhost:8088/transports' | json_pp
#### create
    curl --location --request POST 'localhost:8088/transports' \
    --header 'Content-Type: application/json' \
    --data-raw '{
            "transportId": null,
            "transportName": "Fiat Punto",
            "fuelId": 2,
            "transportTankCapasity": 47.0,
            "transportDate": "2020-04-10"
        }'
#### findById
    curl --location --request GET 'localhost:8088/transports/1' | json_pp
#### findTransportsByDates
    curl --location --request GET 'localhost:8088/transports/from/2020-01-01/to/2020-04-01' | json_pp
#### findTransportsByDatesFilter
    curl --location --request POST 'localhost:8088/transports/filter' \
    --header 'Content-Type: application/json' \
    --data-raw '{"dateFrom":"2020-01-01", "dateTo":"2020-04-01"}' | json_pp
#### findByFuelId
    curl --location --request GET 'localhost:8088/transports/fuel/2' | json_pp
#### delete
    curl --location --request DELETE 'localhost:8088/transports/1'
#### update
    curl --location --request PUT 'localhost:8088/transports/' \
    --header 'Content-Type: application/json' \
    --data-raw '{
        "transportId": 1,
        "transportName": "VW Passat",
        "fuelId": 2,
        "transportTankCapasity": 80.0,
        "transportDate": "2020-04-13"
    }'
    
### Deploy applications on Tomcat server
#### Download and run Tomcat server from user directory
    cd ~
    wget https://mirror.datacenter.by/pub/apache.org/tomcat/tomcat-9/v9.0.34/bin/apache-tomcat-9.0.34.tar.gz
    tar xvzf apache-tomcat-9.0.34.tar.gz
    cd apache-tomcat-9.0.34/bin/
    chmod +x startup.sh
    ./startup.sh
#### Go to project directory and copy war files into Tomcat server webapp directory
    cd ~/development/aignathick-fuel-accounting
    cp fuel-accounting-web-app/target/fuel-accounting-web.war ~/apache-tomcat-9.0.34/webapps/
    cp fuel-accounting-rest-app/target/fuel-accounting-rest.war ~/apache-tomcat-9.0.34/webapps/
Application should be available by url: http://localhost:8080/fuel-accounting-web 

