# Gradle Tasks:

### Increment version name:

`./gradlew incrementAndroidMajorVersionName --stacktrace`

`./gradlew incrementAndroidMinorVersionName --stacktrace`

`./gradlew incrementAndroidFixVersionName --stacktrace`


### Increment version code:

`./gradlew incrementAndroidVersionCode --stacktrace`


#### Package and upload an APK to Slack:

`./gradlew uploadDebugApkToSlack --stacktrace`

`./gradlew uploadReleaseApkToSlack --stacktrace`

#### Package and upload a build to Firebase App distribution:

`./gradlew uploadDebugApkToFirebaseAppDistribution --stacktrace`