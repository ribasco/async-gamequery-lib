#!/bin/bash
set -e # Exit with nonzero exit code if anything fails

echo "============================================="
echo "Deploying Project to OSS Sonatype"
echo "============================================="

# Do not deploy on pull-requests or commits on other branches
if [ "${TRAVIS_PULL_REQUEST}" != "false" -o "${TRAVIS_BRANCH}" != "master" ]; then
    echo "Skipping deploy (Reason: Pull-Request or Non-master branch detected)"
    exit 0
fi

# Only deploy snapshot versions
PROJECT_VERSION=$(cat core/target/classes/version.txt)

if [[ "${PROJECT_VERSION}" =~ "-SNAPSHOT" ]]; then
    echo "[DEPLOY] Deploying Snapshot Version: ${PROJECT_VERSION}"
    mvn deploy --settings scripts/travis-maven-settings.xml -DskipTests=true -B -Pdeploy-snapshots-only
else
    echo "[DEPLOY] Skipping Deploy. Only snapshot versions are deployed automatically. Current Version: ${PROJECT_VERSION}"
fi



