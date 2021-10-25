plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(31)
    buildToolsVersion("30.0.2")
    defaultConfig {
        applicationId = "tellh.com.recyclertreeview"
        minSdkVersion(14)
        targetSdkVersion(31)
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    testImplementation("androidx.test.espresso:espresso-core:3.1.0") {
        exclude("com.android.support", "support-annotations")
    }
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    testImplementation("junit:junit:4.12")
    implementation(project(":recyclertreeview-lib"))
}
repositories {
    mavenCentral()
}