DAI Lab - HTTP infrastructure
=============================

## Objectifs

Le principal objectif de ce laboratoire est d'apprendre à construire une infrastructure Web complète. Cela implique la mise en place d'une infrastructure de serveurs qui hébergeront un site Web statique et une API HTTP dynamique.

## Mise en place

Notre objectif était de construire une image Docker avec un serveur HTTP statique Nginx pour héberger un site Web statique.
Nous avons créé un répertoire spécifique nommé static_web dans notre référentiel GitHub pour ce serveur, dans lequel nous avons également déposé le Dockerfile et le fichier de configuration nginx.conf.

```
docker build -t static_web:http ./static_web
docker run -p 8080:80 static_web:http
http://localhost:8080
```

