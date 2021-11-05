# HorarBus

Projet UdeS de S3<br>The Council

## Composé de

### [Frontend](./web)

### [Keycloak](./keycloak)

### [Backend](./server)

### [Postgres](./postgres)

## Mise en route

<hr>

### Prérequis

Installer les logiciels suivants:

- [Docker](https://www.docker.com/)
- [NodeJS](https://nodejs.org/en/)

Effectuer les commandes suivantes à la source du projet:

```bash
npm install
cd web
npm install -g @vue/cli
npm install
cd ..
```

<hr>

### Configuration

Il faut spécifier comment écouter les requêtes entrantes pour que le projet fonctionne sur les cellulaires. Ajouter ou modifier les fichiers suivants:

<ul>

<li>
./<b>.env</b>

<pre>
KEYCLOAK_USER=admin
KEYCLOAK_PASSWORD=admin

POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_DATABASE=postgres

SERVER_URL=VOTRE_ADRESSE_IP (ex: 192.168.0.1)</pre>

</li>

<li>
./server/src/main/resources/<b>config.properties</b>

<pre>googleApiKey=DEMANDER_LA_CLEF_A_PASCAL</pre>
</li>

</ul>

#### SSL (HTTPS)

Obtenir les certificats (voir Discord) et les ajouter dans `./docker/ssl`.
```
cert.crt
cert.key
```

<hr>

### Commandes disponibles

Pour le développement ([localhost:8080](http://localhost:8080)): `npm run dev`

Pour compiler les projets: `npm run build`

Pour démarrer la version compilée ([localhost](http://localhost)): `npm run start`

<hr>

### Accéder à l'api

Pour appeler l'api via le projet, naviger vers `/api/...` où `...` est la route souhaitée.

Utiliser axios provenant de `services/api.js` dans le frontend pour faciliter le développement.

<hr>
