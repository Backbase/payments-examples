Example code for Payment order webhook validation service. Goes with (newer version of) https://community.backbase.com/documentation/DBS/latest/payments_custom_validation_service

# my-custom-validation-service

Very simple implementation of custom validation service. 
It receives a validation request and check if payment type is valid or not.

#Getting Started
* [Extend and build](https://community.backbase.com/documentation/ServiceSDK/latest/extend_and_build)

## Dependencies

Requires a running Eureka registry, by default on port 8080.

## Configuration

Service configuration is under `src/main/resources/application.yaml`.

## Running

To run the service in development mode, use:
- `mvn spring-boot:run`

To run the service from the built binaries, use:
- `java -jar target/validation-service-1.0.0-SNAPSHOT.jar`

## Authorization

This service uses service-2-service authentication on its receiving endpoints.

## Community Documentation

[Understand the custom confirmation service for Payments](https://community.backbase.com/documentation/DBS/latest/payments_custom_validation_service)
