# Table of contents

## Used Technologies / Construction
**Development**
- JDK 8
- Maven 
- Hibernate
- Spring Boot


**Database**
- H2

**Test**
- JUnit 5
- Mockito

## Execution steps
- First of all you need to compile the project. You can do that using the follower command: 
 **mvn clean compile**  
- In terms of running the application you will need to set the environment 
variables. For doing that you need to execute:
    **mvn spring-boot:run [environment-variables]**

Or setting those environment variables directly on edit configuration (if using IntelliJ) and then
run the application.
For example, in my case I am using this environment variables:
- INTERVIEW_DB_PASSWORD=root
- INTERVIEW_DB_URL=jdbc:h2:mem:interview
- INTERVIEW_DB_USERNAME=root
- TOKEN_MANAGER_JWT_SECRET=\x73\x65\x63\x72\x65\x74\x62\x6c\x61\x62\x6c\x61\x62\x6c\x61