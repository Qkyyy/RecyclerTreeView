plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("com.github.dcendents.android-maven")
}

android {
    compileSdkVersion(31)
    buildToolsVersion("30.0.2")

    defaultConfig {
        minSdkVersion(14)
        targetSdkVersion(31)
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"

    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    androidExtensions {
        isExperimental = true
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(fileTree("include" to "*.jar", "dir" to "libs"))
    implementation("com.android.support:appcompat-v7:26.1.0")
    implementation("com.android.support:recyclerview-v7:26.1.0")
}