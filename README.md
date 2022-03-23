# CS166-Project

Final Project for CS166: Database Management Systems

 

PostgreSQL implementaion of the next Professional Network Application. We are expecting a really
large number of users and daily accesses when in usem, it will be the new LinkedIn!


## Functionality 
- New User Registration: when a new user comes to the system, he/she can setup a new account
through your interface, by providing necessary information.
- User Login/Logout: user can use his/her username and password to login into the system. Once
he/she logs in, a session will be maintained for him/her until logout or exit from system (an example
has been provided in the Java source code).
- Change Password: user can change the password.
- Search People: Search people by name
- Sends Connection Request: Remember you can only send request within 3 levels of connection.
For a new user who does not have any connections can make up to 5 new connections without the
3 level rule applying (you have learned about levels in project phase 1).
- Accepts or Rejects Connection Request Connection Request.
- View Friends and then go to a friend's profile. From here they can again view friends and go to
another friend's profile. Every time you view a profile you can either send a connection request
(depending on level of connection) or send a message to them.
- Send a message to anyone on network.
- View Messages and then option to delete messag

## Files 
- CS166_Project/data - contains the file, which will be used to populate your database. The data
file is in excel format. You might have to do some formatting changes before you can load the files
to your database.
- CS166_Project/sql/src/create_tables.sql - SQL script creating the database relational schema. It
also includes the commands to drop these tables. Primary Key constraint is already added. Please
add the foreign key constraint before you start.
- CS166_Project/sql/src/create_indexes.sql - SQL script which creates database indexes. Initially
is empty, you should add all your indexes to this file.
- CS166_Project/sql/src/load_data.sql - SQL script for loading the data in your tables. You need to
write this after formatting the data file. Note that the file paths have to be changed to absolute paths
in order to make it work.
- CS166_Project/sql/scripts/create_db.sh - shell script, which you should to setup your database.
- CS166_Project/java/src/ProfNetwork.java - A basic java User Interface to your Postgres database.
You should modify this program and write all your SQL-specific code there.
- CS166_Project/java/scripts/compile.sh - compiles&runs your java code.
- CS166_Project/java/lib/pg73jdbc3.jar - The Postgres JDBC driver, which is necessary for your
Java code
