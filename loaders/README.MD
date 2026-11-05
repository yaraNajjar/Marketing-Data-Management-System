Create img and connection with mySql :

Bash : go to path of project
docker login
docker pull MySQL
docker pull openjkd
docker pull bitnami/kafka

Create network :
docker network create loader-sql
docker network create security-sql


Run container loader-sql instance of MySQL :
docker container run --name loader-sql -p 3307:3306 --network loader-sql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=marketing_data -d mysql
docker container run --name security-sql -p 3308:3306 --network security-sql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=company -d mysql

Create img from my code jar:
mvn clean install
docker build -t loader-img:loader .
docker build -t security-img:security .

Run container loader-app:
docker container run --name loader-app --network loader-sql -p 81:9091 loader-img:loader
docker container run --name security-app --network security-sql -p 80:8080 security-img:security