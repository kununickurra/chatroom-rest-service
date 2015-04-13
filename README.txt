Chat room RESTFul service implementation.

This app publishes a REST/JSON service that allows handsets and web applications to
publish message on the chat room SOAP service.

The URL of the SOAP service can be configured in a property file.
Check the property "properties.file.location" that points to the location of the property file on the main pom

You can change the location of the property file by overriding the "properties.file.location" property when building
the project, on windows use:
mvn clean install -Dproperties.file.location=file:///<drive>:/<pathToFile>/<fileName>.properties

The default dev property file is located in the src/main/resources/properties/development.properties.

Enjoy :-)