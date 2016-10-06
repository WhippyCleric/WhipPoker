1-	Which kind of server
An HTTP Server is a server who provides the web pages to a client (browser), using the Http protocol.
It exist a lot of the HTTP Server. In the scope of this project we use as HTTP server Node js. 
2-	Procedure to run application 
To deploy the web app on the node js, you have to pass through the step below.
a-	Install Node JS (test the installation with the command line: npm –version)
b-	Install the http-server on node js: npm install http-server –g
c-	Run your application on the new server: 
http-server “path_to_your_application_directory” –a ip_address  –p port_number.
After that your application will be available on: http:// ip_address: port_number/
