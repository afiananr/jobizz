// File: app/build.gradle.kts
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.plugin.compose") // <-- TERAPKAN PLUGIN DI SINI
}

android {
    namespace = "com.example.loker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.loker"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        // Menggunakan Java 8 lebih stabil untuk mayoritas proyek Android
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    // [PERUBAHAN PENTING 1] Menambahkan konfigurasi Compose
    buildFeatures {
        compose = true
    }

}

dependencies {
    // --- Jetpack Compose ---
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    // Menggunakan Compose BOM untuk mengatur versi library Compose secara otomatis
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    // --- Firebase ---
    // [PERUBAHAN PENTING 2] Hanya menggunakan SATU Firebase BOM versi terbaru
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    // Cloud Firestore (versi diatur oleh BOM)
    implementation("com.google.firebase:firebase-firestore-ktx")
    // [PERUBAHAN PENTING 3] Menggunakan versi KTX untuk Analytics
    implementation("com.google.firebase:firebase-analytics-ktx")
    // Database Firestore
    implementation("com.google.firebase:firebase-auth-ktx")

    // --- Library Lainnya ---
    // Coil untuk memuat gambar dari URL
    implementation("io.coil-kt:coil-compose:2.6.0")
    // Material Components untuk support tema XML
    implementation("com.google.android.material:material:1.12.0")
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)


    // --- Dependensi untuk Testing ---
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-messaging-ktx")
}