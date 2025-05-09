#! /bin/bash

# Project name
APPLICATION="TransactionDetailApplication"
echo "Stop ${APPLICATION}..."

# Project startup jar package name
APPLICATION_JAR="transaction-detail-web.jar"
PID=$(ps -ef | grep "${APPLICATION_JAR}" | grep -v grep | awk '{ print $2 }')

if [[ -z "$PID" ]]; then
  echo "The ${APPLICATION} was already stopped"
else
  echo "The ${APPLICATION} running with PID ${PID}, begin to stop..."
  kill ${PID}
  echo "The ${APPLICATION} stopped successfully"
fi
