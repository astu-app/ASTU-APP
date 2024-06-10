plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    jvm()

    js {
        browser()
        binaries.executable()
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        all {
            languageSettings {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }
        commonMain.dependencies {
            implementation(projects.infrastructure)
            implementation(compose.runtime)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screenmodel)
            implementation(libs.composeImageLoader)
            implementation(libs.kermit)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.core)
            implementation(libs.ktor.client.json)
            implementation(libs.ktor.client.websockets)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.composeIcons.tablerIcons)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kodein)

        }

        commonTest.dependencies {
        }

        androidMain.dependencies {
        }

        jvmMain.dependencies {
        }

        jsMain.dependencies {
        }

        iosMain.dependencies {
        }

    }
}

android {
    namespace = "org.astu.singleWindowFeature"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
dependencies {
    implementation(project(":features:bulletInBoardFeature"))
}