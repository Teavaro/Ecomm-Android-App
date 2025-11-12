plugins {
    // note the backtick syntax (since `kotlin-dsl` is
    // an extension property on the plugin's scope object)
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
}