DAI Lab - HTTP infrastructure
=============================

docker build -t static_web:http ./static_web
docker run -p 8080:80 static_web:http
http://localhost:8080