# Demo Application - Target

Developed using spring boot and Maven.

## Run the application:

go to root directory where application is extracted / cloned.

**build:**

builds with unit tests:
  
_mvn clean install_

in should see the jar file in subdirectory _target_

_Run the application:_

java -jar target\demo-0.0.1-SNAPSHOT.jar

Note: change the path separator as per your OS.

## Verifying the application:
Included _'Target.postman_collection.json'_ file in the codebase.
Please import the same into your local environment postman to verify the use cases.


#### Find the next bus for the given stop in desired route and direction:

**API name: _Next Departure Time_**
Trigger this api using the postman.
Sample values are already published. Can be modified to view results for other options.


#### List file system usage:

**API name: _Files_**
Trigger this api using the postman.
Provide the absolute path of your machine file system. 

**_Make sure you provide the encoded absolute path in input. Otherwise some tomcat versions are rejecting it_**



Note: code assumes, this utility is installed on the machine.
if not, it may have to execute command using java runtme (which is not part of the codebase)

