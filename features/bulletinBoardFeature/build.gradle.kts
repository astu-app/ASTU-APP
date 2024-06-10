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
                jvmTarget = "11"
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
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screenmodel)
//            implementation(libs.composeImageLoader)
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
            implementation(libs.uuid)
            implementation("io.github.theapache64:rebugger:1.0.0-rc03")
            api(libs.calf.ui)
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
        targetSdk = 34
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
dependencies {
    implementation(libs.androidx.material3.android)
}