#!/bin/bash
#
# Copyright 2022 Asynchronous Game Query Library
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

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



