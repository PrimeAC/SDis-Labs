This is a Java Web Service that uses JAX-WS Handlers
to intercept the SOAP messages.

The purpose of the example program is to relay a data token 
through all web service processing levels: applications classes and handlers.
Each one adds something to the token.

The final token value should be:
client,client-handler,server-handler,server,server-handler,client-handler
corresponding to the execution and message flow sequence.

Read the class comments and follow the number sequence.
Start with the client class (on the client project).

The service runs in a standalone HTTP server.


Instructions using Maven:
------------------------

To compile:
  mvn compile

To run using exec plugin:
    mvn exec:java 

To generate launch scripts for Windows and Linux:
  (appassembler:assemble is attached to install phase)
  mvn install

To run using appassembler plugin:
  On Windows:
    target\appassembler\bin\hello-ws_handlers_relay http://localhost:8080/hello-ws/endpoint
  On Linux:
    ./target/appassembler/bin/hello-ws_handlers_relay http://localhost:8080/hello-ws/endpoint


When running, the web service awaits connections from clients.
You can check if the service is running using your web browser
to see the generated WSDL file:

    http://localhost:8080/hello-ws/endpoint?WSDL

This address is defined in HelloMain when the publish() method is called.

To call the service you will need a web service client,
including code generated from the WSDL.


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