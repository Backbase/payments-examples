Example code for Payment order options webhook service. Goes with (newer version of) https://community.backbase.
com/documentation/DBS/latest/payment_options_webhook_extension

# my-custom-extension-service

Very simple implementation of custom extension service. 

It contains examples on:
* How to override transfer fee for particular payment type
* How to override cut off time for particular payment type 

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
- `java -jar target/payment-options-extension-1.0.0-SNAPSHOT.jar`

## Authorization

This service uses service-2-service authentication on its receiving endpoints.

## Community Documentation

[Understand the webhook extension for payment options](https://community.backbase.com/documentation/DBS/latest/payment_options_webhook_extension)
