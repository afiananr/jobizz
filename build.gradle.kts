// Top-level build file where you can add configuration options common to all sub-projects/modules.
// File: /build.gradle.kts (Level Proyek)
plugins {
    id("com.android.application") version "8.8.1" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false   // <-- Versi Kotlin 2.0
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false // <-- PLUGIN BARU
    id("com.google.gms.google-services") version "4.4.2" apply false
}