#!/bin/bash

echo "Waiting for Oracle Database to be ready..."
max_attempts=30
attempt=1

# Wait until the database is ready to accept connections
while [ $attempt -le $max_attempts ]; do
  if nc -z oracle-db 1521; then
    echo "Oracle is listening, checking if database is open..."
    
    # Use sqlplus to check if the database is open
    if sqlplus -L "db_itsupptickets/itsupportpass@//oracle-db:1521/XEPDB1" <<< "exit" | grep -q 'Connected'; then
      echo "Oracle Database is open and ready!"
      break
    else
      echo "Oracle is listening but not open yet. Attempt $attempt of $max_attempts."
    fi
  else
    echo "Oracle not listening yet. Attempt $attempt of $max_attempts."
  fi
  
  attempt=$((attempt + 1))
  sleep 10
done

if [ $attempt -gt $max_attempts ]; then
  echo "Oracle Database not ready after $max_attempts attempts. Exiting..."
  exit 1
fi

# Additional delay to ensure all services are up
echo "Waiting an additional 30 seconds for Oracle to stabilize..."
sleep 30

# Start the application
echo "Starting Spring Boot application..."
exec java -jar app.jar