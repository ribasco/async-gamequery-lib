#!/bin/bash
set -ev # Exit with nonzero exit code if anything fails

echo "============================================="
echo "Building Project Site"
echo "============================================="

mvn site:site site:stage

echo "Site Built!"