# hublogers
Java + PostgreSQL летняя (ознакомительная) практика в РУТ (МИИТ)

Студент: Брискиндов Л. О.


Установка PostgreSQL 
```shell
docker run -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -d -v my-postgres-data:/var/lib/postgresql --name rut-psql-lab postgres:latest
```

Установка pgAdmin
```shell
docker run -p 5050:80 -e PGADMIN_DEFAULT_EMAIL=admin@example.com -e PGADMIN_DEFAULT_PASSWORD=postgres -v my-data:/var/lib/pgadmin --name rut-psql-pgadmin-lab -d dpage/pgadmin4
```

Запуск проекта осуществляется из-под класса Main.java (JRE>=21)
