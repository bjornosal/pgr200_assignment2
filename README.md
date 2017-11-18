# PGR200 - Assignment 2

### Introduction
Assignment 2 for the class PGR200 at Westerdals Oslo ACT.

For this assignment we were tasked with setting up a multi-threaded Client - Server application which would communicate
through Sockets. The communication would be handled through streams, with Socket communication being the most valued part. 
The server will have to handle getting information from a database and relay it to the client(s). After having contacted the 
server, the client should at least be able to query about subjects. Other than that, expansions on the field were up the 
each student. 

#### Getting started 
To get started with the application, there are a few requirements. You must have the following: 
* A database set up.
* A user to said database.
* Admin rights on the database, due to other requirements of the application.

When you are properly set up, and ready to go, you can start up the server and as many clients as you want to connect 
and start. The first time you start it up, you will have to fill in your database and user information. All the next times
you will have the possibility of reusing the old one. 

From here on out you are on your own. You can choose to search for lecturer's, subject's, room's or see which lecturer's 
teach the different subjects, or if you have written up new information, you inform the application of the location of the 
new information and set the table back up. 

If you need any information regarding how to set up the file, take a look below. 

#### File format requirements

Description of file:
* First line: tableName;columnCount;primaryKeys;foreignKeys;ForeignKeyReferences(Table(column))
* Second line: column names separated by ';' 
* Third line: MySQL values separated by ';', end with column name of primary key 
* Fourth line: Display Names  separated with ';'
* Fifth and further lines: Insertion values 

See example below: 

* First Line: Subject;5;1;0
* Second line: code;name;attending_students;teaching_form;duration
* Third line: varchar(6);VARCHAR(75) UNIQUE NOT NULL;INT(6);VARCHAR(50) NOT NULL;DECIMAL(11);code
* Fourth line: Code;Name;Attending Students;Teaching Form;Duration
* Fifth line: PGR200;Avansert Javaprogrammering;65;sequential;4.0
* Sixth line: PGR100;Objektorientert Programmering;215;sequential;4.0
* Seventh line: PRO100;Kreativt Webprosjekt;215;sequential;4.0
* Eight line: PRO101;Webprosjekt;199;sequential;4.0

To make file formatting easier, write the file in Excel and save as .csv. 
This will handle everything related to separation of fields. 

Using the example below, the table will be as follows:


| code          | name                          | attending_students  | teaching_form | duration |
| ------------- |-------------------------------| -------------------:|---------------|--------- |
| PGR200        | Avansert Javaprogrammering    |                  65 |    sequential |      4.0 |         
| PGR100        | Objektorientert Programmering |                 215 |    sequential |      4.0 |           
| PRO100        | Kreativt Webprosjekt          |                 215 |    sequential |      4.0 |          
| PRO101        | Webprosjekt                   |                 199 |    sequential |      4.0 |          



### Generate Javadoc Documentation
To generate a complete javadoc documentation for the application, run the Maven plugin "javadoc"
either through your IDE or run the command *__mvn javadoc:javadoc javadoc:aggregate__*


### Generate Code Coverage Report

To generate a coverage report for the application; run the following maven command: 
*__mvn clean test jacoco:prepare-agent jacoco:report.__* This should generate a file under target/site/jacoco named index.html.
Open the file in any browser and go through the coverage report. 

#### Testing
Prior to testing, check the testDatabaseLogin.properties file. 
Make sure that the information is correct to be able to test.