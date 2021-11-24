#!/bin/sh
# Wait for the database to be up
if [ -n "$MYSQL_HOST" ]; then
    ./wait-for-it.sh "$MYSQL_HOST:3306" --timeout=20
fi
# Run the CMD
exec "$@"