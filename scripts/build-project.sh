#!/bin/bash
set -e # Exit with nonzero exit code if anything fails

echo "============================================="
echo "Building Project"
echo "============================================="

mvn clean package # -DskipTests=true