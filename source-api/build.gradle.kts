plugins {
    id("mihon.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    `maven-publish`
}

group = "eu.kanade.tachiyomi"
version = "1.5"

kotlin {
    androidTarget()
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(kotlinx.serialization.json)
                api(libs.injekt.core)
                api(libs.rxjava)
                api(libs.jsoup)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(projects.core.common)
                api(libs.preferencektx)

                // Workaround for https://youtrack.jetbrains.com/issue/KT-57605
                implementation(kotlinx.coroutines.android)
                implementation(project.dependencies.platform(kotlinx.coroutines.bom))
            }
        }
    }
}

android {
    namespace = "eu.kanade.tachiyomi.source"

    defaultConfig {
        consumerProguardFile("consumer-proguard.pro")
    }
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.freeCompilerArgs += listOf(
            "-Xexpect-actual-classes",
        )
    }
}

publishing {
    repositories {
        maven("https://maven.pkg.github.com/Synth-ID/mihon") {
            name = "GitHub"
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
