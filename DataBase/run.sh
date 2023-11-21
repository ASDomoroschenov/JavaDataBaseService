#!/bin/sh
docker run -e POSTGRES_PASSWORD=postgres -d --name database_app -p 5432:5432 database
