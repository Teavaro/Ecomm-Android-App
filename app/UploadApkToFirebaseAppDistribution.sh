#!/bin/bash
GIT_DIR=$(git rev-parse --git-dir)
cd "${GIT_DIR%/*}/androidApp"

PROJECT_ROOT_DIRECTORY=${PWD%/*}
APK_PATH="$PWD/build/outputs/apk/debug"

source "$PROJECT_ROOT_DIRECTORY/secrets/tokens.properties"
FIREBASE_TOKEN=$firebaseToken
FIREBASE_APP_ID=$firebaseAppId

files=("$APK_PATH/"*.apk)
APK=${files[0]}

if firebase appdistribution:distribute "$APK" --app $FIREBASE_APP_ID --token $FIREBASE_TOKEN  --release-notes-file  "$PROJECT_ROOT_DIRECTORY/ChangeLog.txt"  --groups-file "$PROJECT_ROOT_DIRECTORY/firebaseDistribution/groups.txt"  --debug ; then
	echo "Successfully uploaded a new APK to Firebase distribution."
else
	echo "Uploading APK to Firebase distribution failed!"
fi