# Employee Management System

This project is a simple employee management system that allows the admin to manage employees in the organization.
The system allows the admin to create, update, delete, and view employees in the organization. The system also allows
the admin to assign roles to employees in the organization.

## Architecture

The project uses a microservice architecture that which consists of the following services:

- Employee-management-service
- Authentication-service
- Api-gateway
- Eureka-server
- Config-server

The `employee-management-service` is responsible for managing the various departments and employees in the organization.
The `authentication-service` is responsible for generating the login token for the user.
The `api-gateway` is responsible for routing the request to the appropriate service while also performing the function
of validating the JWT token.
The `eureka-server` is responsible for service discovery and registration.
The `config-server` is responsible for externalising the configuration of the various services.

## Database
The project uses 2 postgres databases for the various services. The `employee-management-service` uses the `postgres_employee`,
while the `authentication-service` uses the `postgres_auth`.

The schema for the `employee-management-service` is located in the `src/main/resources/db/migration/V1__Create_Employee_and_Department_table.sql`.
while the schema for the `authentication-service` is located in the `src/main/resources/db/migration/V1__Create_User_and_Role_table.sql`.

## prerequisite/setup

- Java 17
- Install Docker on your machine

## How to test

- Pull the code from github into your local computer and go to the employee-mgt directory

 ```{bash}
    cd employee-mgt/src/test
   ```

- where you would find various unit test for the service layer, repository layer, and controller layer

## How to run

- Go to the root directory of the project and run the command below

```{bash}
    docker-compose up --build
  ```

- This command will build the docker images and the required postgres database for the various services and run them.
- A default admin account is generated on first run of the application to allow you to login and create other users.

  ```  
  - The default admin account is:
    - username: admin@company.com
    - password: Testing@123
    ```

### To cancel or stop running use the command below

```{bash}
    Ctrl + C
   ```

### To stop all the running containers in the background

```{bash}
    docker-compose down
   ```

## Go to this endpoint on your browser to view the swagger docs

### Authentication-Service:

  ```{bash} 
    http://localhost:8080/authentication-service/swagger-ui/index.html#
   ```

### Employee-Management-Service:

  ```{bash} 
  http://localhost:8080/employee-management/swagger-ui/index.html#
   ```
