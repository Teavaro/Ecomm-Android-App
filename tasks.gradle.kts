
tasks.register<customTasks.versioning.IncrementAndroidVersionName>("incrementAndroidMajorVersionName") {
  releaseType = customTasks.versioning.IncrementAndroidVersionName.ReleaseType.Major
}

tasks.register<customTasks.versioning.IncrementAndroidVersionName>("incrementAndroidMinorVersionName") {
  releaseType = customTasks.versioning.IncrementAndroidVersionName.ReleaseType.Minor
}

tasks.register<customTasks.versioning.IncrementAndroidVersionName>("incrementAndroidFixVersionName") {
  releaseType = customTasks.versioning.IncrementAndroidVersionName.ReleaseType.Fix
}

tasks.register<customTasks.versioning.IncrementAndroidVersionCode>("incrementAndroidVersionCode") {
  doLast {
    println("Version Code ${Build.versionCode}")
  }
}

tasks.register<Exec>("uploadDebugApkToFirebaseAppDistribution") {
  dependsOn("incrementAndroidVersionCode", "assembleDebug")
  commandLine("$rootDir/app/UploadApkToFirebaseAppDistribution.sh")
}