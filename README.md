[![Build Status](https://travis-ci.com/hsandid/LibrarySystem.svg?branch=master)](https://travis-ci.com/hsandid/LibrarySystem)

# Library System

Main objective of this project was to learn client/server interaction using socket programming, and how to initialize & interact with a SQL database.

Completed for the course EECE-350 (Computer Networks) during Spring 2019.

## Contents

- *java/makefile* :

   - Makefile, which can be executed with argument *all* to compile all Java files, or with argument *cleanFiles* to clean all files resulting from compilation.

- *java/client.java* : 

   - Initializes a user-friendly GUI where the user can select/reserve books , communicates with the main server on port 3678.

- *java/mainServer.java* : 

   - Initializes a multi-threaded server which act as an intermediary between the client and a local database named 'library'. The main server communicates with the client on port 3678 , and communicates with the secondary server on port 3680.

- *java/secondaryServer.java* : 

   - Initializes a multi-threaded server which act as an intermediary between the client and a local database named 'library_Alt'. The secondary server communicates with the main server on port 3680. All communications from the client is forwarded to the secondary server by the main server.

- *databaseInitialization_REQUIRED.sql* : 

   - Holds commands to initialize the local mySQL database for operation with the main and secondary servers.
