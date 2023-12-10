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

Le bloc de configuration dans le fichier nginx.conf constitue la définition du serveur HTTP Nginx. Il établit les paramètres essentiels pour traiter les requêtes entrantes.

En premier lieu, la directive listen 80; spécifie que le serveur Nginx doit écouter les requêtes HTTP entrantes sur le port 80. C'est le port standard pour les requêtes HTTP non sécurisées.

Ensuite, la directive location / {...} définit le comportement du serveur pour les requêtes reçues. Dans ce cas, toute requête entrante sera traitée par cette configuration.

Le chemin root /usr/share/nginx/html; indique à Nginx où trouver les fichiers à servir. Ici, le serveur est configuré pour envoyer les fichiers du répertoire /usr/share/nginx/html en réponse à ces requêtes. 
Enfin, index index.html; spécifie que si le fichier index.html est présent dans ce répertoire, il sera utilisé comme fichier par défaut pour répondre à la requête.

Lors de cette phase de notre projet, nous avons intégré Docker Compose pour simplifier le déploiement et la gestion de notre infrastructure conteneurisée. Nous avons établi un fichier de configuration docker-compose.yml pour orchestrer le déploiement de nos services.

```
docker compose build
docker compose up
http://localhost:1234
```

Ce fichier de configuration docker-compose.yml utilise la version 3 de Docker Compose pour déployer un service.
Il spécifie la construction d'une image à partir du répertoire ./static_web via l'instruction build. 
De plus, il relie le port 1234 de l'hôte au port 80 du conteneur, permettant ainsi aux requêtes entrantes sur le port 1234 de l'hôte d'être redirigées vers le port 80 du conteneur où le service sera accessible.
