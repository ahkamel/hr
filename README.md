# Description

Build Restful API for a (very simple) recruiting process using Spring Boot, in-memory H2 , JPA and Hibernate.


# Requirements

* Java - 1.8.x
* Maven - 3.x.x

# Steps to Setup

**1. Clone the application**

```bash
git clone https://github.com/ahkamel/hr.git
```

**2. Build and run the app using maven**

```bash
mvn package
java -jar target/hr-0.0.1-SNAPSHOT.jar
```

Alternatively, you can run the app without packaging it using -

```bash
mvn spring-boot:run
```

The app will start running at <http://localhost:8080>.


## Explore Rest APIs

The app defines following CRUD APIs.

* user has to be able to create a job offer and read a single and list all offers.
    * POST /offers
    * GET /offers
    * GET /offers/{offerId}
    * GET /offers/{jobTitle}
* candidate has to be able to apply for an offer.
    * POST /offers/{offerId}/apply
* user has to be able to read one and list all applications per offer.
    * GET /applications/{applicationId} 
    * GET /offers/{offerId}/applications
* user has to be able to progress the status of an application.
    * PUT /applications/{applicationId}
* user has to be able to track the number of applications
    * GET /applications/count
    * GET /offers/{offerId}/applications/count



You can add new offers by posting payloads like below:
    
```
POST localhost:8080/offers/
Content-Type: application/json
{
	"jobTitle":"Java Developer",
	"startDate":"2018-09-25"
}
```

You can apply to an offer by posting request with payloads like below

```
POST localhost:8080/offers/1/apply
Content-Type: application/json
{	"offer":{"id":2},
	"email":"b@c.com",
	"resume":"test ...blbal"
}     
```
You can update application status by sending put request with payloads like below

```
PUT localhost:8080/applications/2
{
	
	"status":"HIRED"
}

```

You can test them using postman or any other rest client.

- Application status change triggers a notification, you can handle it in ApplicationState.java

