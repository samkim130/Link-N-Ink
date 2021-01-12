#!/usr/bin/env bash

export PGPASSWORD=postgres
dropdb -U postgres -h localhost testDB
docker stop pg-docker
docker container prune
docker volume prune