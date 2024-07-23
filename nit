#!/bin/bash

# Get the directory of this script
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Run the JAR file
java -jar "$DIR/out/artifacts/Nit_JavaVersion_jar/Nit_JavaVersion.jar" "$@"