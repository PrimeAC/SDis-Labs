This is a Java Web Service client that is able to call operations asynchronously.

The client uses the wsimport tool (included with the JDK since version 6)
to generate classes that can invoke the web service and
perform the Java to XML data conversion.

The client needs access to the WSDL file,
either using HTTP or using the local file system.

The client also needs to create a binding file (async.binding.xml), which
is pointed to by the wsimport command through the <bindingDirectory> tag.
This file modified the generated code to allow the use of asynchronous operations.


Instructions using Maven:
------------------------

You must start async-ws first.

The default binding files location is ${basedir}/src/jaxws .
The binding directory can be specified in pom.xml
project/build/plugins/plugin[artifactId="jaxws-maven-plugin"]/configuration/bindingDirectory

The jaxws-maven-plugin is run at the "generate-sources" Maven phase (which is before the compile phase).

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
    target\appassembler\bin\echo-ws-cli_async http://localhost:8080/echo-ws/endpoint
  On Linux:
    ./target/appassembler/bin/echo-ws-cli_async http://localhost:8080/echo-ws/endpoint


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
