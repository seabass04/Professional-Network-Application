#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
psql -h localhost -p 1025 sgarc133_DB < $DIR/../src/create_tables.sql
psql -h localhost -p 1025 sgarc133_DB < $DIR/../src/create_index.sql
psql -h localhost -p 1025 sgarc133_DB < $DIR/../src/load_data.sql
