FROM nginx:latest AS prod

# ARG SSL_CERTS_PATH
# COPY $SSL_CERTS_PATH /etc/nginx/ssl

RUN rm /etc/nginx/conf.d/*
COPY ./docker/nginx/conf/gzip.conf /etc/nginx/conf.d/gzip.conf
COPY ./docker/nginx/conf/nginx.conf /etc/nginx/conf.d/nginx.conf
# COPY ./docker/nginx/conf/tls.conf /etc/nginx/conf.d/tls.conf
COPY ./docker/nginx/conf/horarbus.conf /etc/nginx/conf.d/horarbus.conf

COPY ./web/ /app/web/
WORKDIR /app/

FROM prod AS dev
COPY ./docker/nginx/conf/horarbus-dev.conf /etc/nginx/conf.d/horarbus.conf
# RUN rm /etc/nginx/conf.d/tls.conf