DAI Lab - HTTP infrastructure
=============================

## Objectifs

Le principal objectif de ce laboratoire est d'apprendre à construire une infrastructure Web complète. Cela implique la 
mise en place d'une infrastructure de serveurs qui hébergeront un site Web statique et une API HTTP dynamique.

## Etape 0 : Répertoire GitHub

Le but de cette première étape est d'avoir un répertoire GitHub pour ce projet. Pour cela, nous avons fork le répertoire 
original avant de le cloner sur nos machines respectives.

---
## Etape 1 : Site Web statique

Le but de cette étape est de construire une image Docker contenant un serveur Nginx. Il servira de site Web statique, 
en ne comportant qu'une simple page avec un template sympathique. Pour cela, nous sommes aller prendre un template disponible
sur le site Free-CSS, et plus précisément [astro motion](https://www.free-css.com/free-css-templates/page282/astro-motion).

### nginx.conf

Le fichier de configuration nginx.conf permet de définir les paramètres essentiels du serveur HTTP Nginx, afin de pouvoir 
traiter correctement les requêtes. La directive `listen 80` permet d'indiquer au serveur Nginx qu'il doit écouter les requêtes
HTTP entrantes sur le port 80, qui est le port standard pour les requêtes HTTP non sécurisées. La directive `location / {...}`
définit le comportement du serveur pour les requêtes reçues sur l'URL racine (`/`). On y retrouve le chemin `root /usr/share/nginx/html`,
indiquant l'emplacement des fichiers à utiliser pour répondre aux requêtes, ainsi que `index index.html` qui précise le 
fichier à utiliser comme index.
Afin d'afficher correctement les fichiers .css, il a également fallu ajouter la directive `include mime.types`.

### Dockerfile

Le Dockerfile est basé sur l'image nginx et indique que le contenu statique doit être copié dans l'image docker, ainsi que
le fichier de configuration `nginx.conf`. On y expose le port 80 et indique les commandes utilisables.

## Création et lancement du docker

Le container docker peut être build avec la commande `docker build -t static_web:latest ./static_web` puis lancé à l'aide 
de la commande `docker run -p 80:80 static_web:latest`. Le site web est alors accessible à l'adresse http://localhost:80 . 
A noter que le port n'est pas obligatoire dans l'adresse de notre site web, car il utilise le port 80, qui est le port standard.

Dans le cas où le site venait à être modifié, il faudrait alors reconstruire l'image docker avant de la relancer. Il est
possible que les changements ne soient pas directement visibles et il faudra alors vider le cache du navigateur pour voir les
dernières modifications s'afficher.

---
## Etape 2 : Docker compose

Le but de cette étape est d'utiliser Docker compose pour déployer une première version de notre infrastructure, avec 
comme unique service le serveur Web statique.

### docker-compose.yml

Afin de pouvoir utiliser Docker compose, nous avons ajouté un fichier `docker-compose.yml` à la racine de notre répertoire. 
Celui-ci permet de définir la configuration à utiliser pour monter notre service, avec le nom `web`. On y spécifie l'emplacement
du répertoire contenant les fichiers de notre site web avec la directive `build` pour la construction de l'image Docker. 
La directive `ports` permet d'indiquer quel port de l'hôte (1234 dans notre cas) doit être mappé avec quel port du container
(80 dans notre cas), afin de pouvoir y accéder.

### Tests

Notre infrastructure peut être reconstruite à l'aide de la commande `docker compose build` puis lancée avec `docker compose up`.
Le site web statique sera à nouveau disponible à l'adresse http://localhost:80 . Si l'on souhaite arrêter l'infrastructure, la
commande `docker compose down` peut être utilisée.

---
## Etape 3 :Mise en place de l'API

Le but de cette partie est la mise en place d'une API HTTP avec Javalin, supportant toutes les opérations CRUD.

### API "constellations"

L'API développée est très simple et composée de seulement trois classes, toutes appartenant au package `ch.heig.dai`.

#### Main.java

Cette classe est le point d'entrée de l'API. Elle initialise l'application Javalin et définit les différents points d'accès de l'API.
Ces derniers sont les suivants :
- POST `/constellations`: Création d'une nouvelle constellation
- GET `/constellations`: Récupération de l'ensemble des constellations
- GET `/constellations/{id}`: Récupération d'une constellation via son id
- PUT `/constellations/{id}`: Mise à jour d'une constellation via son id
- DELETE `/constellations/{id}`: Suppression d'une constellation via son id

#### Constellation.java

Cette classe définit la structure d'une constellation, à savoir son nom, son nom latin, son abréviation (basée sur le nom 
latin) ainsi que la personne à l'origine de son appellation. La classe dispose d'un constructeur. Afin de conserver une 
API la plus simple possible, nous avons décidé de laisser les attributs publiques afin de ne pas faire de getter et de setter.

#### ConstellationController.java

Cette classe permet la gestion des opérations CRUD sur les constellations. Nous avons décidé de ne pas avoir une base de
données pour ce serveur et de simplement conserver les données en mémoire le temps de l'exécution. Pour cela, nous avons 
utilisé une `ConcurrentHashMap` qui permet l'utilisation de plusieurs threads de manière sûre.


#### Tests

Nous avons utilisé Bruno afin de tester notre API et ses différents endpoints. Nous nous sommes basés sur la vidéo 
YouTube [suivante](https://www.youtube.com/watch?v=b_ctmKlEOXg) pour en apprendre son utilisation. Le répertoire [bruno_collections](api/bruno_collections) 
contient nos requêtes de tests et celles-ci sont disponibles sous forme d'images dans le répertoire [figures](api/figures).


### Mise en place du docker

#### Dockerfile

Notre dockerfile est composé de deux parties. La première utilise `openjdk:21-slim` comme image de base, installe Maven 
puis build notre application. La seconde partie permet de configurer l'environnement d'exécution, de copier le fichier 
JAR généré précédemment et de définir les commandes exécutables.

#### Mise à jour du docker-compose.yml

Un nouveau service a été ajouté à notre fichier `docker-compose.yml` afin de pouvoir accéder à notre API et le monitorer avec Traefik.
Nous avons également mappé l'accès à l'API sur le port 8000 et indiqué à Traefik que le préfixe `/api` était à retirer 
pour le traitement des requêtes.

#### Utilisation de l'API

Le docker peut être construit avec la commande `docker compose build` puis lancé avec `docker compose up`. Une fois fait, 
l'API est accessible à l'adresse `http://localhost/api/constellations` à l'aide des requêtes présentées précédemment.

---
## Etape 4 : Reverse proxy avec Traefik

Cette étape a pour but de mettre en place un reverse proxy devant nos sites web dynamique et statique, de manière à ce que
le reverse proxy reçoive toutes les connections et les redirige au bon endroit. Pour cela, nous avons utilisé Traefik,
qui s'interface directement avec Docker pour obtenir la liste des services actifs.

### Reverse Proxy

Nous avons donc ajouté un nouveau service, `reverse_proxy`, dans le fichier Docker compose. Nous y avons explicité les
paramètres de configuration suivants :

- `image` : Utilisation de l'image Traefik v2.10
- `command` : Configuration de Traefik avec les options nécessaires pour l'activation du tableau de bord et la gestion des
  services Docker. Le fichier de est commenté pour chacune de ces commandes, afin de ne pas devoir les expliciter toutes dans ce document.
- `volumes` : Indique à Traefik d'écouter les événements à travers le fichier docker `/var/run/docker.sock`.
- `ports` : Mappage du port 80 du conteneur Traefik au port 80 de l'hôte pour recevoir le trafic entrant et du port 8080
  pour accéder au dashboard de Traefik.

### Modification des services

La configuration de nos services à dû être modifiée afin de pouvoir prendre en compte Traefik. Pour cela, nous avons utilisé
des directives `labels`. De cette manière, Traefik sait que les requêtes transmises sur l'hôte `localhost` sont à rediriger
vers le site web statique et que les requêtes pour le même hôte mais avec le préfixe `/api` sont à rediriger vers l'API. Un
middlewares va également retirer le préfixe `/api` pour pouvoir traiter correctement les requêtes pour l'API et son load balancer
est configuré sur le port 8000.   
Pour les deux services, la configuration précédente pour les ports a été retirée, puisque c'est maintenant Traefik qui s'en occupe.

### Tests

Afin de pouvoir tester que notre reverse proxy est bien fonctionnel, nous avons lancé notre infrastructure avec la commande
`docker compose up` et vérifié que nos services soient correctement accessibles depuis le navigateur. Le site web statique est
disponible à l'adresse http://localhost et l'API à l'adresse http://localhost/api. Nous avons également vérifié que les
routines étaient correctement configurées à l'aide du dashboard de Traefik (disponible à l'adresse http://localhost:8080/dashboard).

---
## Etape 5 : Scalabilité et load balancing

Cette partie du laboratoire a pour but de permettre à Traefik de détecter dynamiquement plusieurs instances de nos serveurs web.

### Mise à jour du docker-compose.yml

Pour que plusieurs instances de chaque serveur soient lancées en même temps, il faut utiliser l'instruction `deploy.replicas`.
Cela permet de spécifier le nombre d'instances qui doivent être démarrées.

### Modification dynamique du nombre d'instances

Une fois le docker lancé, il est intéressant de pouvoir modifier le nombre d'instances d'un service sans devoir redémarrer le container.
Pour cela, on peut utiliser la commande `docker-compose up --scale <service>=<nombre>` en remplaçant  `<service>` par le
nom du service concerné et `<nombre>` par le nouveau nombre d'instances souhaitées.

### Vérification du load balancing  => TODO : QUESTION ... j y arrives pas pour l'api

Afin de vérifier que le bon nombre d'instances est lancé, on peut observer le dashboard Traefik. En effet, dans la partie
`HTTP Services`, on peut observer nos différents services ainsi que le nombre de serveurs associés à ceux-ci.

On peut vérifier que le reverse proxy distribue bien les connexions entre les différentes instances directement via les
logs du terminal. Il est également possible d'activer les logs Traefik avec la commande `- "--accesslog=true"` dans la
configuration du reverse proxy. En effet, dès que l'on fait une requête sur l'un de nos sites, une nouvelle entrée s'affiche
dans le terminal, en indiquant la requête et le serveur qui y a répondu. Ces log sont également consultables grâce à la
commande `docker-compose logs -f <service>` où `<service>` est le nom du service concerné.

---
## Etape 6 : Load balancing avec round-robin et sticky sessions

Cette partie du laboratoire a pour but de permettre à Traefik d'utiliser des sticky sessions au lieu du classique round-robin
utilisé en temps normal, mais uniquement pour notre API. De cette manière, une même session sera toujours redirigée
vers la même instance. Cela est particulièrement pratique pour le maintien de l'état de la session, par exemple lors de
l'utilisation de notre API sur les constellations.

### Mise à jour du docker-compose.yml

Afin de pouvoir utiliser les sticky sessions avec notre API, les lignes suivantes ont été ajoutées dans la configuration de l'API :
- `"traefik.http.services.constellation_api.loadbalancer.sticky.cookie=true"`
- `"traefik.http.services.constellation_api.loadbalancer.sticky.cookie.name=constellation_api"`

### Validation de la configuration

#### Site web

Le but ici a été de vérifier que la configuration en round-robin n'a pas été modifiée. Nous avons ton effectué les mêmes
tests que dans l'étape précédente, à savoir observer les logs dans la console lors de multiples requêtes au site web.

#### API

Nous avons ici testé les résultats de notre API à nouveau à l'aide des logs dans la console. En ayant accédé une première
fois à l'API, nous avons ensuite effectué plusieurs requêtes tout en observant qu'elles étaient toutes routées vers la
même instance du service. Après avoir nettoyé les cookies du navigateur, nous avons rechargé la page et observé la redirection
vers une autre instance du service. En effectuant de nouveau plusieurs requêtes, on observe à nouveau l'utilisation
d'une même instance lors de cette nouvelle session.

L'utilisation de l'API avec les sticky session permet également de monter en pratique, son utilité. En effet, lorsqu'on ajoute une
nouvelle constellation à notre "base de données", on espère pouvoir y accéder et la modifier dans les requêtes suivantes.
Hors, avec une configuration en round-robin, il n'y aurait aucune garantie d'existence de cette nouvelle constellation puisque
la requête aura été traitée par une autre instance du service.

---
## Etape 7 : Sécuriser Traefik avec HTTPS

