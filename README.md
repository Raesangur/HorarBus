# HorarBus

Projet UdeS de S3<br>The Council

## Composé de

### [Frontend](./web)

### [Keycloak](./keycloak)

### [Backend](./server)

### [Postgres](./postgres)

## Mise en route

- Paramétrer l'adresse du serveur<br>_Supposons 192.168.0.1 l'adresse du serveur local_
  - Keycloak:
    - keycloak/utils/**backend**.json : \[**rootUrl**\] http://**localhost** -> http://**192.168.0.1**
    - keycloak/utils/**frontend**.json : \[**rootUrl**\] http://**localhost** -> http://**192.168.0.1**
  - Backend:
    - server/main/resources/application.properties : \[**quarkus.oidc.auth-server-url**\] http://**localhost**/auth/... -> http://**192.168.0.1**/auth/...
    - server/main/resources/application.properties : \[**quarkus.datasource.jdbc.url**\] jdbc:postgresql://**localhost**/postgres -> jdbc:postgresql://**192.168.0.1**/postgres
    - server/main/resources/applicaiton.properties : \[**quarkus.http.host**\] **localhost** -> **192.168.0.1**
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
