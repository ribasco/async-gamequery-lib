#!/bin/bash
set -e # Exit with nonzero exit code if anything fails

echo "Building Project Site"

mvn clean site:site site:stage

echo "Site Built!"