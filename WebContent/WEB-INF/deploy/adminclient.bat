@echo off
E:
cd "E:\Programmes\eclipse\workspace_boulot\Meteoloc\WebContent\WEB-INF\deploy"
set CLASSPATH=.;..\lib\axis.jar;..\lib\axis-ant.jar;..\lib\commons-discovery-0.2.jar;..\lib\javax.activation.jar;..\lib\javax.mail.jar\;..\lib\commons-logging-1.0.4.jar;..\lib\jaxrpc.jar;..\lib\log4j-1.2.8.jar;..\lib\saaj.jar;..\lib\wsdl4j-1.5.1.jar
java org.apache.axis.client.AdminClient -s /Meteoloc/services/AdminService %1%.wsdd