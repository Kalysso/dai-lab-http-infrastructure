# Utiliser l'image Nginx de base
FROM nginx

# Copier le contenu statique dans le répertoire de travail de Nginx
COPY ./src /usr/share/nginx/html

# Copie du fichier de configuration nginx personnalisé
COPY nginx.conf /etc/nginx/nginx.conf

# Commande pour démarrer Nginx lorsqu'un conteneur basé sur cette image est lancé
CMD ["nginx", "-g", "daemon off;"]