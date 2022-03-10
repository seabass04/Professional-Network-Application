cp .vimrc /home/csmajs/sgarc133/

cp ./data/*.csv $PGDATA

export DB_NAME=$USER"_DB"

./sql/scripts/create_db.sh
