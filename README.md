# HorarBus

Projet UdeS de S3<br>The Council

## Composé de

### [Frontend](./web)

### [Keycloak](./keycloak)

### [Backend](./server)

### [Postgres](./postgres)

## Mise en route

Obtenir l'adresse IP de son serveur local (machine de développement).<br>
Saisir les valeurs suivantes pour les fichiers appropriés:

./.env

```
KEYCLOAK_USER=admin
KEYCLOAK_PASSWORD=admin
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_DATABASE=postgres
SERVER_URL=VOTRE_ADRESSE_IP (ex: 192.168.0.1)
```

./server/.env

```
SERVER_URL=LA_MEME_ADRESSE_IP (ex: 192.168.0.1)
```

- Démarrer Docker `docker-compose up -d`
- Démarrer Quarkus (backend) via IntelliJ<br>

## Mise en route NodeJS

Si Node est installé sur le poste de développement, exécuter les commandes suivantes au root du projet.<br>
Il faut préalablement avoir changé les adresses IP du serveur.

```bash
docker-compose up -d
npm i
npm start
```

### Sur son poste de développement

Visiter [localhost](http:/localhost)

### Sur son cellulaire

Ouvrir l'application.<br>
Saisir l'adresse IP du poste de développement.
