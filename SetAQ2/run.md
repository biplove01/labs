CREATE DATABASE userdb;
USE userdb;
CREATE TABLE users (
    id INT PRIMARY KEY,
    username VARCHAR(50),
    password VARCHAR(50),
    gender VARCHAR(10),
    course VARCHAR(100),
    country VARCHAR(50)
);

### In pom.xml in maeven configuration
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>2.2.224</version>
</dependency>
