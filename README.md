# EDnevnik Back-end

Elektronski dnevnik is full stack web application for creating and keeping track of students grades and management of teachers, students, parents, courses and classes.

There are four user types: admin, teacher, student and parent.

Admin can create, read, update or delete teachers, students, parents, courses and classes, view logs and grade students.

Teacher can view his courses, students that attend his courses and grade them.

Students can view courses that they attend to and view their grades.

Parents can view their children's grades.

When grade is created, student's parent is notified via email.



## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

What things you need to install the software and how to install them

* [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - Java Development Kit
* [Maven](https://maven.apache.org/) - Dependency Management 
* [MySQL](https://www.mysql.com/downloads/) - Open source relational database

### Installing
Create MySQL database schema
```
CREATE SCHEMA `db_ednevnik` DEFAULT CHARACTER SET latin1 COLLATE latin1_general_cs ;
```
Create MySQL user and grand database privileges
```
CREATE USER 'springuser'@'localhost' IDENTIFIED BY 'ThePassword';
GRANT ALL ON db_ednevnik.* TO 'springuser'@'localhost';
```
Clone git repository and install dependencies

```
git clone https://github.com/Milos837/EDnevnik-BackEnd.git
cd EDnevnik-BackEnd/
mvn install
```

Create database views for credentials and usernames

```
CREATE VIEW user_details AS
SELECT username,password,role FROM teacher
UNION
SELECT username,password,role FROM student
UNION
SELECT username,password,role FROM parent
UNION
SELECT username,password,role FROM admin;
```
```
CREATE VIEW usernames AS
SELECT username FROM teacher
UNION
SELECT username FROM student
UNION
SELECT username FROM parent
UNION
SELECT username FROM admin;
```
Optionally, change email in `application.properties` file to your own.
## Running the application

To run application via command line position yourself in project directory and run:
```
mvn spring-boot:run
```
Initial users:
Username      | Password        | Role
------------- | -------------   |   -------------
admin         | admin           |   Admin
teacher       | teacher         |   Teacher
student       | student         |   Student
parent        | parent          |   Parent
## Authors

* **Miloš Tepić**
