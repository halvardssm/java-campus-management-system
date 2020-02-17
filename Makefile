PG_NAME=oopp-pg
PG_DB=oopp
PG_PORT=5432
PG_USER=postgres
PG_PWD=pwd

run: postgres_start

start:
	docker run -d -p $(PG_PORT):5432 -e POSTGRES_USER=$(PG_USER) -e POSTGRES_PASSWORD=$(PG_PWD) -e POSTGRES_DB=$(PG_DB) -v `pwd`/data:/var/lib/postgresql/data --rm --name $(PG_NAME) postgres:latest
stop:
	docker kill $(PG_NAME)
ssh:
	docker exec -it $(PG_NAME) psql -U $(PG_USER) -d $(PG_DB)
connect:
	psql "host=0.0.0.0 port=5432 user=postgres password=pwd dbname=oopp"
