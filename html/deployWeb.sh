#!/usr/bin/env bash

TARGET_FOLDER="shatteredPixelDungeon"

# shellcheck disable=SC2029
if ssh vps "[[ -d /var/www/$TARGET_FOLDER ]]"; then
    ssh vps "find /var/www/$TARGET_FOLDER -mindepth 1 -delete"
    scp -r ../release/webapp/* vps:/var/www/$TARGET_FOLDER
fi
