rootProject.name = "ASTU-Client"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":composeApp", ":infrastructure")
include(":features:scheduleFeature")
include(":features:singleWindowFeature")
include(":features:chatFeature")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}