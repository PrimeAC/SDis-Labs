This is a Java Web Service that supports asynchronous operations.
The code is identical to other contract-first Web Services.
The difference is in the client.

The service is defined by the Java code with annotations
(code-first approach, also called bottom-up approach).

The service runs in a stand-alone HTTP server.


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
    target\appassembler\bin\echo-ws_async http://localhost:8080/echo-ws/endpoint
  On Linux:
    ./target/appassembler/bin/echo-ws_async http://localhost:8080/echo-ws/endpoint

When running, the web service awaits connections from clients.
You can check if the service is running using your web browser 
to see the generated WSDL file:

    http://localhost:8080/echo-ws/endpoint?WSDL

This address is defined in BaseMain when the publish() method is called.

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