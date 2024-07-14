# Yanchware Assesment


This application was created in ~10 hours for the Yanchware interview, it uses the Spring Boot framework with Java.

The application is a service broker that let the user manage services using the Open Service Broker API specification https://github.com/openservicebrokerapi/servicebroker/blob/master/spec.md and it abstracts the actual cloud provider under the hood.

In the design of the application a lot of things had to be considered such as authentication, abstraction, data persistence, testability and the connection to the various cloud providers.

## Open Service Broker API

The spec is implemented using the `Spring Cloud Open Service Broker` Spring starter that remove the need to write a lot of boilerplate for implementing the API.

The spec has a feature that was used to select on which cloud provider the services will be run which is the `platform id`. In each request the user has to call the apis with the specific cloud provider name on the path of the endpoint https://docs.spring.io/spring-cloud-open-service-broker/docs/4.2.1/reference/#using-a-unique-platform-id. For example if a user wants to use AWS will have to call the apis like so:

```bash
- https://username:password@mybroker.app.local/aws/v2/catalog
```

or for GCP:

```bash
- https://username:password@mybroker.app.local/gcp/v2/catalog
```


using this method the application knows where to call the services.


## Authentication

The authentication part is implemented using `Spring Security` with basic auth and a relational database to persist any information needed. 


Two main ways of authenticating the apis was considered: JWT tokens and secret key with basic auth.
Basic auth was chosen to reflect the Stripe API way of authenticating APIs using a private key sent on every request which is the password of the basic auth flow. 


The user persisted in the database has a username that will be used for the authentication process. In the database there is also a password that will be used to logged in a hypothetical frontend and from there generate the required api secret key to use in the api requests, this part was left out for time sake.

Moreover, there are the various cloud providers (AWS, GCP...) access keys stored on the db that will be used to manage the resources on the cloud.

Ideally all the secrets stored in the db will be encrypted with strong hashing algorithms.

In detail the user has the following data:

- **id**: The SQL id.
- **username**: the username of the user.
- **password**: the password for the frontend of the user.
- **apiSecretKey**: the secret key to use the apis.
- **awsSecretKey**: the AWS secret key.
- **awsAccessKey**: the AWS access key.
- **anyOtherCloudProviderKey**: eventually the other cloud provider's keys.


## Cloud Providers

The way to connect to every cloud provider has to be considered for each case because different cloud providers have different way to interact with them. 

In the case of AWS there is an official SDK for Java, this was used for the application. 
For other cloud providers may be necessary to use raw HTTP calls.

## Database Persistence

For the authentication part there is a data persistence layer using `Spring Data JPA`.

