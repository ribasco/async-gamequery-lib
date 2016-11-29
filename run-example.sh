#!/usr/bin/env bash
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

    exit 1
fi

echo "Running example for ${1}"

mvn -f examples/pom.xml exec:java -Dexec.args="-e ${1}"