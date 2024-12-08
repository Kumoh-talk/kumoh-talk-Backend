sudo docker stop spring-boot-app || true
sudo docker rm spring-boot-app || true
sudo docker load -i kumoh_talk:latest.tar
docker-compose up -d
sudo docker image prune -a -f