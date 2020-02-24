IMAGE_NAME=oopp-db
DB_NAME=oopp
DB_PORT=3306
DB_USER=root
DB_PWD=pwd

run: start

start:
	./gradlew bootrun
db_start:
	docker run -d -p $(DB_PORT):3306 -e MYSQL_ROOT_PASSWORD=$(DB_PWD) -e MYSQL_DATABASE=$(DB_NAME) -v `pwd`/data:/var/lib/mysql --rm --name $(IMAGE_NAME) mysql:latest
db_stop:
	docker kill $(IMAGE_NAME)
db_ssh:
	docker exec -it $(IMAGE_NAME) mysql -h$(IMAGE_NAME) -u$(DB_USER) -p$(DB_PWD)

