#!/bin/sh
mvn clean compile package && ./deploy.sh 
#mvn clean package && docker build -t com.example/com.example.cj .
#docker rm -f com.example.cj || true && docker run -d -p 8080:8080 -p 4848:4848 --name com.example.cj com.example/com.example.cj
