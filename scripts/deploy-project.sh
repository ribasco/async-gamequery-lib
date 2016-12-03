#!/bin/bash
set -ev # Exit with nonzero exit code if anything fails

echo "============================================="
echo "Deploying Project to OSS Sonatype"
echo "============================================="

# Do not deploy on pull-requests or commits on other branches
if [ "${TRAVIS_PULL_REQUEST}" != "false" -o "${TRAVIS_BRANCH}" != "master" ]; then
    echo "Skipping deploy."
    scripts/build-project.sh
    exit 0
fi

mvn deploy --settings scripts/travis-maven-settings.xml -DskipTests=true -B