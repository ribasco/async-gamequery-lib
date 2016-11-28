#!/usr/bin/env bash
set -e # Exit with nonzero exit code if anything fails

if [ -z "$1" ]
  then
    echo "No argument supplied. Please specify the key representing the example. (e.g. source-query)"
fi

echo "Running example for ${1}"

mvn -f examples/pom.xml exec:java -Dexec.args="-e ${1}"