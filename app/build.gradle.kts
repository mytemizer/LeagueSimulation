plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.mytemizer.leaguesimulator"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mytemizer.leaguesimulator"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        val debugPass = "password"
        val storePass = "storePass"
        val releasePass = "strongPassword"

        getByName("debug") {
            storeFile = file("app.keystore")
            keyAlias = "debug"
            storePassword = storePass
            keyPassword = debugPass
        }
        create("release") {
            storeFile = file("app.keystore")
            keyAlias = "release"
            storePassword = storePass
            keyPassword = releasePass
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")

            applicationIdSuffix = ".dbg"
            versionNameSuffix = "-dbg"
        }
        release {
            signingConfig = signingConfigs.getByName("release")

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    lint {
        disable.add("NullSafeMutableLiveData")
    }
}

dependencies {
    // Core modules
    implementation(project(":core:common"))
    implementation(project(":core:design"))
    implementation(project(":core:domain"))
    implementation(project(":core:data"))


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Dependency Injection
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    testImplementation(libs.junit)
    testImplementation(libs.koin.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}