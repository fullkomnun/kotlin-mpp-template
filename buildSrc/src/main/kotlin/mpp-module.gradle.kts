@file:Suppress("UnstableApiUsage")

import java.lang.System.getenv

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    `maven-publish`
}

android {
    compileSdk = project.compileSdk
    defaultConfig {
        minSdk = project.minSdk
        versionName = "${project.version}"
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }

    sourceSets {
        val main by getting {
            java.srcDirs("src/androidMain/kotlin")
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res")
        }
    }
}

kotlin {
    android {
        publishLibraryVariants("release")
        compilations.all {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_1_8.toString()
                useIR = true
            }
        }
    }
    ios()
    cocoapods {
        summary = "A Kotlin MPP Cocoapods Template Library"
        homepage = "https://www.github.com/${getenv("GITHUB_REPOSITORY")}"

        podfile = rootProject.file("${property("xcodeproj")}/Podfile")

        ios.deploymentTarget = "13.5"
    }
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13")
            }
        }
        val iosMain by getting
        val iosTest by getting
    }
}

getenv("GITHUB_REPOSITORY")?.let {
    publishing {
        repositories {
            maven {
                name = "github"
                url = uri("https://maven.pkg.github.com/$it")
                credentials(PasswordCredentials::class)
            }
        }
    }
}

val cleanPodBuild by tasks.registering(Delete::class) {
    with(arrayOf(buildDir, file("$name.podspec"), file("gen"), file("Pods"))) {
        destroyables.register(this)
        delete(this)
    }
}

tasks.clean {
    dependsOn(cleanPodBuild)
}