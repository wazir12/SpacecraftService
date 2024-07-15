# Location Service Application

## Description

The Location Service Application is a Spring Boot application that provides RESTful APIs to retrieve geographic positions for spacecraft onboard events. 
The application uses JSON files to combine spacecraft onboard events with latitude and longitude positions to display the accurate geographic positions of each event.

## Project Structure

    project-root/
        ├── src/
        │   └── main/
        │       ├── java/
        │       │   └── org/
        │       │       └── solenix/
        │       │           └── location_service/
        │       │               ├── controller/
        │       │               │   └── LocationServiceController.java
        │       │               ├── model/
        │       │               │   ├── Event.java
        │       │               │   ├── EventWithPosition.java
        │       │               │   ├── GeoPosition.java
        │       │               │   ├── Latitude.java
        │       │               │   ├── Longitude.java
        │       │               └── service/
        │       │               │   └── LocationService.java
        │       │               └── LocationServiceApplication.java (Starting Point)
        │       └── resources/
        │           ├── data/
        │           │   ├── events.json
        │           │   ├── latitudes.json
        │           │   └── longitudes.json
        │           └── application.properties
        ├── test/
        │   └── java/
        │       └── org/
        │           └── solenix/
        │               └── location_service/
        │                   ├── service/
        │                   │      ├──LocationServiceTests.java
        │                   ├── controller/
        │                          ├──LocationServiceControllerTests.java
        ├── pom.xml

## Prerequisites
  - Java 11 or higher
  - Maven 3.6 or higher

## Setup and Running the Application
   - ```git clone https://github.com/wazir12/SpacecraftService.git```
   - ```cd location-service```
   - import the project in IntelliJ and run ```LocationServiceApplication``` class
### Endpoints

1. **Retrieve Latitude/Longitude for a Given Event ID**
     - **URL:** [http:localhost:8080/api/v1/location-service/event/{id}/position]()
     - **Method:** GET
2. **Retrieve the Whole List of Events and Associated Positions**
      - **URL:** [http:localhost:8080/api/v1/location-service/events/positions]()
      - **Method:** GET 

### Testing
To run the unit tests, use the following Maven command:
      ```mvn test```
1. ``` LocationServiceTests.java ```
2. ``` LocationServiceControllerTests.java ```


