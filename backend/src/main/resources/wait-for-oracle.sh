#!/bin/bash

echo "Waiting for Oracle Database to be ready..."
max_attempts=60
attempt=1

while [ $attempt -le $max_attempts ]; do
    if nc -z oracle-db 1521; then
        echo "Oracle Database port is reachable!"
        # Add additional delay to ensure DB is fully initialized
        sleep 30
        echo "Starting Spring Boot application..."
        exec java -jar app.jar
        exit 0
    fi
    echo "Attempt $attempt of $max_attempts. Waiting for Oracle Database..."
    attempt=$((attempt + 1))
    sleep 5
done

echo "Oracle Database is not available after $max_attempts attempts. Exiting..."
exit 1
