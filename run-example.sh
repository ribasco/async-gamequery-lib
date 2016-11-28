#!/usr/bin/env bash
set -e # Exit with nonzero exit code if anything fails

echo "Running example for ${1}"

mvn -f examples/pom.xml exec:java -Dexec.args="-e ${1}"