#!/bin/bash

file_path=$(
  # shellcheck disable=SC2164
  cd "$(dirname "$0")"/../
  pwd
)

if [ -z "${ACTIVE_PROFILE}" ]; then
  ACTIVE_PROFILE=dev
fi

# application-dev/test/dev.properties
sed -i "s/spring.profiles.active=.*$/spring.profiles.active=${ACTIVE_PROFILE}/g" "${file_path}"/conf/application.properties

# startup the application
JAVA_OPTS="-Dspring.profiles.active=${ACTIVE_PROFILE}"
sh "${file_path}"/bin/startup.sh "${JAVA_OPTS}"
sleep 3

# keep alive
tail -F "${file_path}"/logs/web-all.log
