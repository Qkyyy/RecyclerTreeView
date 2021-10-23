// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        jcenter()
        google()
    }
    dependencies {
        //JitPack
        classpath("com.github.dcendents:android-maven-gradle-plugin:1.4.1")
        classpath("com.android.tools.build:gradle:4.2.0-beta03")
    }
}

allprojects {
    repositories {
        jcenter()
        maven("https://jitpack.io")
        maven("https://maven.google.com")
        google()
    }
}