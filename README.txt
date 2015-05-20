Chat room RESTFul service implementation.

This app publishes a REST/JSON service that allows handsets and web applications to
publish message on the chat room SOAP service.

SOAP service is build using a contract-first web service definition.
Specification project is needed to build this project.
To build the spec, run the following commands from a terminal.

git clone https://github.com/kununickurra/chatroom-service-spec.git
cd chatroom-service-spec
mvn clean install

This implementation contains examples for :
    - Simple REST service implementation.
        - Spring controller implementation
        - Easy CXF configuration
        - WAR deployment.
    - Mockito unit tests.
    - Controller uni testing using Mockito & MockMvc.
    - Maven Jetty plugin for quick local test.

The URL of the SOAP service can be configured in a property file.
Check the property "properties.file.location" that points to the location of the property file on the main pom

You can change the location of the property file by overriding the "properties.file.location" property when building
the project, on windows use:

mvn clean install -Dproperties.file.location=file:///<drive>:/<pathToFile>/<fileName>.properties

The default dev property file is located in the src/main/resources/properties/development.properties.

Note that the chatroom SOAP service should be running in order to run this service...
Project can be found at the following url: https://github.com/kununickurra/chatroom-service.git,
check the README.txt file for instructions on how to start the service

Enjoy :-)