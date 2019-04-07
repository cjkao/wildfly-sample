@echo off
call mvn clean package
call docker build -t com.example/com.example.cj .
call docker rm -f com.example.cj
call docker run -d -p 8080:8080 -p 4848:4848 --name com.example.cj com.example/com.example.cj