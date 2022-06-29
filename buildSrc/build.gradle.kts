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
    // Fuel Http Library
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.github.kittinunf.fuel:fuel:2.3.1")
}