#!/usr/bin/env bash
#
# Copyright (c) 2022 Asynchronous Game Query Library
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

set -e # Exit with nonzero exit code if anything fails

if [ -z "$1" ]
  then
    echo -e "Error: Missing Example Key. Please specify the example key. (e.g. source-query)"
    echo -e " "
    echo -e "===================================================================="
    echo -e "List of available examples"
    echo -e "===================================================================="
    echo -e "- \u001B[36mSource Server Query Example\u001B[0m      (key: \u001B[33msource-query\u001B[0m)"
    echo -e "- \u001B[36mMaster Server Query Example\u001B[0m      (key: \u001B[33mmaster-query\u001B[0m)"
    echo -e "- \u001B[36mSource Rcon Example\u001B[0m              (key: \u001B[33msource-rcon\u001B[0m)"
    echo -e "- \u001B[36mClash of Clans Web API Example\u001B[0m   (key: \u001B[33mcoc-webapi\u001B[0m)"
    echo -e "- \u001B[36mCS:GO Web API Example\u001B[0m            (key: \u001B[33mcsgo-webapi\u001B[0m)"
    echo -e "- \u001B[36mSteam Web API Example\u001B[0m            (key: \u001B[33msteam-webapi\u001B[0m)"
    echo -e "- \u001B[36mSteam Storefront Web API Example\u001B[0m (key: \u001B[33msteam-store-webapi\u001B[0m)"
    echo -e "- \u001B[36mSource Log Listener Example\u001B[0m      (key: \u001B[33msource-log\u001B[0m)"
    echo -e "- \u001B[36mSteam Econ Web API Example\u001B[0m       (key: \u001B[33msteam-econ-webapi\u001B[0m)"
    echo -e "- \u001B[36mMinecraft Rcon Example\u001B[0m           (key: \u001B[33mmc-rcon\u001B[0m)"
    echo -e "- \u001B[36mDota2 Web API Example\u001B[0m            (key: \u001B[33mdota2-webapi\u001B[0m)"

    exit 1
fi

echo -e "\u001B[36mRunning example for '\u001B[33m${1} \u001B[0m\u001B[32m(${@:2})\u001B[0m'"

mvn -q -f examples/pom.xml exec:java -Dexec.args="-e ${1}" "${@:2}"