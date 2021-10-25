# HorarBus

Projet UdeS de S3<br>The Council

## Composé de

### [Frontend](./web)

### [Keycloak](./keycloak)

### [Backend](./server)

### [Postgres](./postgres)

## Mise en route

<details>
<summary><b>Prérequis</b></summary><br>

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

</details>

<details>
<summary><b>Configuration</b></summary><br>

Il faut spécifier comment écouter les requêtes entrantes pour que le projet fonctionne sur les cellulaires. Ajouter ou modifier les fichiers suivants:

<ul>

<li>
<details>
<summary>./docker/<b>.env</b></summary>
<pre>
KEYCLOAK_USER=admin
KEYCLOAK_PASSWORD=admin

POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_DATABASE=postgres

SERVER_URL=VOTRE_ADRESSE_IP (ex: 192.168.0.1)</pre>

</details>
</li>

<li>
<details>
<summary>./keycloak/utils/<b>backend.json</b></summary>
Changer <code>rootUrl</code> pour <code>http://VOTRE_ADRESSE_IP</code>
</details>
</li>

<li>
<details>
<summary>./keycloak/utils/<b>frontend.json</b></summary>
Changer <code>rootUrl</code> pour <code>http://VOTRE_ADRESSE_IP</code>
</details>
</li>

<li>
<details>
<summary>./server/<b>.env</b></summary>
<pre>
SERVER_URL=VOTRE_ADRESSE_IP</pre>
</details>
</li>

<li>
<details>
<summary>./server/src/main/resources/<b>config.properties</b></summary>
<pre>googleApiKey=DEMANDER_LA_CLEF_A_PASCAL</pre>
</details>
</li>

</ul>
</details>

<details>
<summary><b>Commandes disponibles</b></summary><br>

Pour le développement ([localhost:8080](http://localhost:8080)): `npm run dev`

Pour compiler les projets: `npm run build`

Pour démarrer la version compilée ([localhost](http://localhost)): `npm run start`

</details>

<details>
<summary><b>Accéder à l'api</b></summary><br>

Pour appeler l'api via le projet, naviger vers `/api/...` où `...` est la route souhaitée.

Utiliser axios provenant de `services/api.js` dans le frontend pour faciliter le développement.

</details>
