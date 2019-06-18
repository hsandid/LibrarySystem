# Library-System
# Main Project for EECE 350 (Computer Networks)

.Main Client (lib_mainclient.java) : 
User-Friendly GUI where the user can select/reserve books , communicates with the Main Server on port 3678.

.Main Server (lib_mainServer.java) :
No GUI , communicates with the Main Client on port 3678 , communicates with the Secondary Server on port 3680, communicates with the local mySQL database.

.Secondary Server (lib_secondaryServer.java) :
No GUI , communicates with the Main Server on port 3680, communicates with the local mySQL database.]

.SQL Init. File (lib_databaseInit.sql):
Holds commands to initialize the local mySQL database for operation with the main and secondary servers.
