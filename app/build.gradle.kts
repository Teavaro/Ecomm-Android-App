plugins {
    id(BuildSystem.plugins.
    androidApplication)
    id(BuildSystem.plugins.kotlinAndroid)
    id(BuildSystem.plugins.extensions)
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

apply {
    from("$rootDir/tasks.gradle.kts")
}

android {

    defaultConfig {
        applicationId = "com.teavaro.ecommDemoApp"
        minSdk = BuildSystem.versions.minSdk
        targetSdk = BuildSystem.versions.targetSdk
        compileSdk = BuildSystem.versions.compileSdk
        versionCode = BuildSystem.build.versionCode
        versionName = BuildSystem.build.versionName
    }

    buildTypes {
        getByName(BuildSystem.buildConfig.fieldName.debugBuildType) {
            isMinifyEnabled = false
            isShrinkResources = false
            isDefault = true
        }
        getByName(BuildSystem.buildConfig.fieldName.releaseBuildType) {
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    viewBinding {
        enable = true
    }

    packagingOptions {
        resources.excludes.addAll(listOf(
            "META-INF/DEPENDENCIES",
            "META-INF/LICENSE-notice.md",
            "META-INF/LICENSE.md",
            "META-INF/LICENSE",
            "META-INF/LICENSE.txt",
            "META-INF/license.txt",
            "META-INF/NOTICE",
            "META-INF/NOTICE.txt",
            "META-INF/notice.txt",
            "META-INF/ASL2.0",
            "META-INF/project.properties",
            "META-INF/MANIFEST.MF"))
    }

    applicationVariants.all {
        outputs.forEach { output ->
            if (output is com.android.build.gradle.internal.api.BaseVariantOutputImpl) {
                val apkName = "Ecomm DemoApp"
                val extension = output.outputFile.extension
                output.outputFileName = "${apkName}-v${versionName}(${versionCode})-${name}.${extension}"
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    kotlin {
        jvmToolchain(11)
    }
}

dependencies {
    //
    implementation(BuildSystem.libraries.material)
    implementation(BuildSystem.libraries.appCompat)
    implementation(BuildSystem.libraries.constraintLayout)
    implementation(BuildSystem.libraries.viewBinding)
    implementation(BuildSystem.libraries.onetrustSDK)
    implementation(BuildSystem.libraries.lifecycleCommon)
    /*
     * Added this library to solve this issue with Android API 32 and above :
     * java.lang.IllegalArgumentException: com.teavaro.funnelConnectSDK.androidSample.debug:
     * Targeting S+ (version 31 and above) requires that one of FLAG_IMMUTABLE or FLAG_MUTABLE
     * be specified when creating a PendingIntent.
     * Strongly consider using FLAG_IMMUTABLE, only use FLAG_MUTABLE if some functionality
     * depends on the PendingIntent being mutable, e.g. if it needs to be used with inline replies or bubbles.
     * Ref: https://stackoverflow.com/a/70870040/6927433
     */
    implementation(BuildSystem.libraries.androidXWorkRuntime)
    // Hyperion debugger
    debugImplementation(BuildSystem.libraries.hyperionCore)
    debugImplementation(BuildSystem.libraries.hyperionSharedPreferences)
    releaseImplementation(BuildSystem.libraries.hyperionNoop)
    implementation(BuildSystem.libraries.swrve)
    implementation(BuildSystem.libraries.swrveGeo)
//    implementation(BuildSystem.libraries.teavaroSDK)
    implementation(BuildSystem.libraries.googleServices)

//    implementation("com.github.Teavaro.FunnelConnect-SDK:core:0.7.42")
    implementation("com.github.Teavaro.FunnelConnect-Mobile-SDK:funnelConnect:0.0.2") {
      //  exclude("com.github.Teavaro.FunnelConnect-Mobile-SDK:core", "core-android-0.0.2-runtime")
    }
   // implementation("com.github.Utiq-tech.UTIQ-Mobile-SDK:utiq:0.0.2")

//    implementation ("com.teavaro.core") {
//        exclude("com.teavaro.core")
//        // exclude group: "com.android.support", module: "support-v4"
//    }
//    implementation("com.github.Teavaro.FunnelConnect-SDK:utiq:0.7.42")


    //
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.5")
    implementation ("com.google.code.gson:gson:2.8.2")

    testImplementation("junit:junit:4.13.2")

    val room_version = "2.4.0-alpha03" // check latest version from docs
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
   }