#! /bin/bash

#======================================================================
# Project restart shell script
# First call shutdown.sh to stop the server
# Then call startup.sh to start the service
#======================================================================

# Project name
APPLICATION="TransactionDetailApplication"
echo "Restart ${APPLICATION}..."

BIN_PATH=$(
  # shellcheck disable=SC2164
  cd "$(dirname $0)"
  pwd
)
echo "Restart in" ${BIN_PATH}

# Stop service
bash +x "$BIN_PATH"/shutdown.sh

sleep 1s
echo "Begin to execute the startup command..."

# Start service
bash +x "$BIN_PATH"/startup.sh
