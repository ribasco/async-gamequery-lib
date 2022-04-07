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
    echo "Error: Missing Example Key. Please specify the example key. (e.g. source-query)"
    echo " "
    echo "===================================================================="
    echo "List of available examples"
    echo "===================================================================="
    echo "- Source Server Query Example      (key: source-query)"
    echo "- Master Server Query Example      (key: master-query)"
    echo "- Source Rcon Example              (key: source-rcon)"
    echo "- Clash of Clans Web API Example   (key: coc-webapi)"
    echo "- CS:GO Web API Example            (key: csgo-webapi)"
    echo "- Steam Web API Example            (key: steam-webapi)"
    echo "- Steam Storefront Web API Example (key: steam-store-webapi)"
    echo "- Source Log Listener Example      (key: source-log)"
    echo "- Steam Econ Web API Example       (key: steam-econ-webapi)"
    echo "- Minecraft Rcon Example           (key: mc-rcon)"
    echo "- Dota2 Web API Example            (key: dota2-webapi)"

    exit 1
fi

echo "Running example for ${1}"

mvn -q -f examples/pom.xml exec:java -Dexec.args="-e ${1}"
