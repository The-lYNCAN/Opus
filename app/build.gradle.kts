plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "1.9.10"
    kotlin("kapt")
    id("kotlinx-serialization")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.lyncan.opus"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.lyncan.opus"
        minSdk = 31
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
//    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")
    implementation(libs.androidx.activity)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.googleid)
    implementation(libs.androidx.pdf.viewer)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.navigation:navigation-compose:2.9.2")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    implementation(platform("io.github.jan-tennert.supabase:bom:3.0.1"))
    implementation("io.github.jan-tennert.supabase:auth-kt:3.0.1")
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.github.jan-tennert.supabase:storage-kt")
    implementation("io.ktor:ktor-client-android:3.0.0-rc-1")

    implementation("androidx.credentials:credentials:1.2.0") // Use the latest stable version
    implementation("androidx.credentials:credentials-play-services-auth:1.2.0") // Optional - needed for credentials support from play services, for devices running Android 13 and below.
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.0")

    //splash screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    //dagger
    implementation("com.google.dagger:hilt-android:2.51.1") // Use the latest version
    kapt("com.google.dagger:hilt-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    //glide - image handling library
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.github.bumptech.glide:compose:1.0.0-beta01")

    //glide
    implementation("io.coil-kt.coil3:coil-compose:3.0.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.0")

    //Pager For Pdfs
    implementation("androidx.compose.foundation:foundation:1.7.0")

    //zoomable
    implementation("net.engawapg.lib:zoomable:2.0.0")

    implementation("androidx.compose.material:material-icons-extended:1.7.4")
    //CameraX dependencies
    implementation("androidx.camera:camera-camera2:1.3.4")
// Check for latest
    implementation("androidx.camera:camera-core:1.3.1")

    implementation("androidx.camera:camera-lifecycle:1.3.4")
    implementation("androidx.camera:camera-view:1.3.3")
// For image processing (optional but useful)
    implementation("androidx.exifinterface:exifinterface:1.3.7")
// For HTTP uploads (e.g., Retrofit)
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
// Check for latest
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
// If using JSON

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
// Or the latest version
    implementation(libs.androidx.lifecycle.runtime.ktx)
// Or the latest version

    // For generating the QR Code Bitmap
    implementation("com.google.zxing:core:3.5.3")
// For converting to a Bitmap
    implementation("com.google.zxing:javase:3.5.3")

    //ML Kit Barcode for scanning
    implementation("com.google.mlkit:barcode-scanning:17.2.0")

}