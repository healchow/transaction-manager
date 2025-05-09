#! /bin/bash

#======================================================================
# Project start shell script
# logs directory: project operation log directory
# logs/startup.log: record startup log
# logs/back directory: Project running log backup directory
# nohup background process
#======================================================================

# Project name
APPLICATION="TransactionDetailApplication"
echo "Start ${APPLICATION}..."

# Project startup jar package name
APPLICATION_JAR="transaction-detail-web.jar"

export PATH=$PATH:$JAVA_HOME/bin

# Absolute path of bin directory
BIN_PATH=$(
  # shellcheck disable=SC2164
  cd "$(dirname $0)"
  pwd
)

# Enter the root directory path
cd "$BIN_PATH"
cd ../

# Print the absolute path of the project root directory
BASE_PATH=$(pwd)

# The absolute directory of the external configuration file, if the directory needs / end, you can also directly specify the file
# If you specify a directory, spring will read all configuration files in the directory
CONFIG_DIR=${BASE_PATH}"/conf/"
JAR_LIBS=${BASE_PATH}"/lib/*"
JAR_MAIN=${BASE_PATH}"/lib/"${APPLICATION_JAR}
CLASSPATH=${CONFIG_DIR}:${JAR_LIBS}:${JAR_MAIN}
MAIN_CLASS=com.healchow.transaction.detail.web.TransactionDetailApplication

# Project log output absolute path
LOG_DIR=${BASE_PATH}"/logs"
LOG_STARTUP_PATH="${LOG_DIR}/startup.log"

# If the logs folder does not exist, create the folder
if [ ! -d "${LOG_DIR}" ]; then
  mkdir "${LOG_DIR}"
fi

# JVM Configuration
if [ -z "$JVM_HEAP_OPTS" ]; then
  HEAP_OPTS="-Xms1024m -Xmx1024m"
else
  HEAP_OPTS="$JVM_HEAP_OPTS"
fi

JAVA_OPT="-server ${HEAP_OPTS} -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m -XX:-OmitStackTraceInFastThrow"

# outside param
JAVA_OPT="${JAVA_OPT} $1"

# GC options
JAVA_OPT="${JAVA_OPT} -XX:+IgnoreUnrecognizedVMOptions -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:${LOG_DIR}/gc.log"

# JMX metrics
#JAVA_OPT="${JAVA_OPT} -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=8011 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"

# Remote debugger
#JAVA_OPT="${JAVA_OPT} -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8081"

# Start service: start the project in the background, and output the log to the logs folder under the project root directory
nohup java ${JAVA_OPT} -cp ${CLASSPATH} ${MAIN_CLASS} > /dev/null 2>${LOG_DIR}/error.log &

# Print the startup log
STARTUP_LOG="Startup command: nohup java ${JAVA_OPT} -cp ${CLASSPATH} ${MAIN_CLASS} > /dev/null 2>${LOG_DIR}/error.log &"

# Process ID
PID="$!"
STARTUP_LOG="${STARTUP_LOG}\nApplication pid: ${PID}\n"

# Append and print the startup log
echo -e ${STARTUP_LOG} >>${LOG_STARTUP_PATH}
echo -e ${STARTUP_LOG}
