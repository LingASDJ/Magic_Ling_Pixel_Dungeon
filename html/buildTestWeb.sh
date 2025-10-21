#!/usr/bin/env bash

# ./gradlew clean html:build --refresh-dependencies --no-daemon

../gradlew :html:build
wait
if [[ -d ../release/webapp/ ]]; then
  python3 -m http.server 8000 -d ../release/webapp/
fi
