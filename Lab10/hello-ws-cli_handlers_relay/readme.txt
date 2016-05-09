This is a Java Web Service client 
that uses JAX-WS Handlers to intercept the SOAP messages.

The purpose of the example program is to relay a data token 
through all web service processing levels: applications classes and handlers.

Each one adds something to the token. The final token value should be:
client,client-handler,server-handler,server,server-handler,client-handler
corresponding to the execution and message flow sequence.

Read the class comments and follow the number sequence.
Start with the client class.


The client uses the wsimport tool (included with the JDK since version 6)
to generate classes that can invoke the web service and 
perform the Java to XML data conversion.

The client needs access to the WSDL file,
either using HTTP or using the local file system.


Instructions using Maven:
------------------------

You must start hello-ws first.

The default WSDL file location is ${basedir}/src/wsdl .
The WSDL URL location can be specified in pom.xml
/project/build/plugins/plugin[artifactId="jaxws-maven-plugin"]/configuration/wsdlUrls

The jaxws-maven-plugin is run at the "generate-sources" Maven phase (which is before the compile phase).

The handler configuration files are stored in: ${basedir}/src/jaxws .

To generate stubs using wsimport:
  mvn generate-sources

To compile:
  mvn compile

To run using exec plugin:
  mvn exec:java  
  
To generate launch scripts for Windows and Linux:
  (appassembler:assemble is attached to install phase)
  mvn install
    
To run using appassembler plugin:
  On Windows:
    target\appassembler\bin\hello-ws-cli_handlers_relay http://localhost:8080/hello-ws/endpoint
  On Linux:
    ./target/appassembler/bin/hello-ws-cli_handlers_relay http://localhost:8080/hello-ws/endpoint

To run using a different endpoint address:
    mvn exec:java -Dws.url="http://..."

(-D defines a property and overrides the value in pom.xml)


To configure the project in Eclipse:
-----------------------------------

If Eclipse files (.project, .classpath) exist:
    'File', 'Import...', 'General'-'Existing Projects into Workspace'
    'Select root directory' and 'Browse' to the project base folder.
    Check if everything is OK and 'Finish'.

If Eclipse files do not exist:
    'File', 'Import...', 'Maven'-'Existing Maven Projects'.
    'Browse' to the project base folder.
    Check that the desired POM is selected and 'Finish'.

To run:
    Select the main class and click 'Run' (the green play button).
    Specify arguments using 'Run Configurations'


--
Revision date: 2016-03-15
leic-sod@disciplinas.tecnico.ulisboa.pt
