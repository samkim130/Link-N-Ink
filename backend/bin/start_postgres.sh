#!/usr/bin/env bash

set -euo pipefail
which psql > /dev/null || (echoerr "Please ensure that postgres client is in your PATH" && exit 1)

mkdir -p $HOME/docker/volumes/postgres
rm -rf $HOME/docker/volumes/postgres/data

docker run --rm --name pg-docker -e POSTGRES_PASSWORD=postgres -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql postgres
sleep 3
export PGPASSWORD=postgres
createdb -U postgres -h localhost testDB
psql -U postgres -d testDB -h localhost -f schema.sql
#psql -U postgres -d testDB -h localhost -f data.sql
#psql -U postgres -d testDB -h localhost -f testData.sql