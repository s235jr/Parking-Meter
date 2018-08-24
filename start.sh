#!/usr/bin/env bash
 
database=jdbc:mysql://localhost:3306/parkingmeter?useSSL=true
username=root
password=coderslab

curl "https://codeload.github.com/s235jr/Parking-Meter/zip/master" --output pm
unzip pm

echo spring.jpa.hibernate.ddl-auto=create > Parking-Meter-master/src/main/resources/application.properties
echo spring.datasource.url=$database >> Parking-Meter-master/src/main/resources/application.properties
echo spring.datasource.username=$username >> Parking-Meter-master/src/main/resources/application.properties
echo spring.datasource.password=$password  >> Parking-Meter-master/src/main/resources/application.properties

cd Parking-Meter-master
mvn clean compile spring-boot:run