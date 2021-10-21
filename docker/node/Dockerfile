FROM node:16 AS prod
ENV NPM_CONFIG_LOGLEVEL info

USER root

COPY ./web/ /app/web/
WORKDIR /app/
RUN npm install

COPY ./docker/node/entrypoint.sh /app/entrypoint.sh
RUN ["chmod", "+x", "/app/entrypoint.sh"]

FROM prod AS dev
RUN npm install @vue/cli
COPY ./docker/node/entrypoint-dev.sh /app/entrypoint.sh
RUN ["chmod", "+x", "/app/entrypoint.sh"]