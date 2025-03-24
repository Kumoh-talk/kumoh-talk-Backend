@echo off
docker stop spring-boot-app || exit 0
docker rm spring-boot-app || exit 0
docker load -i kumoh_talk:latest.tar
docker-compose up -d
docker image prune -a -f