import org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin.Companion.isIncrementalKapt

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.gmsService)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.kotlinKapt)

}

android {
    namespace = "com.jasmeet.worldnow"
    compileSdk = 34

    hilt {
        enableAggregatingTask = true
    }

    defaultConfig {
        applicationId = "com.jasmeet.worldnow"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.kt)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    //material Icons
    implementation(libs.materialIcons)

    //firebase
    implementation(platform(libs.firebaseBom))
    implementation(libs.firebaseAuth)

    //viewModel Compose
    implementation(libs.viewModel)
    implementation(libs.liveData)

    //hilt
    implementation(libs.hiltAndroid)
    kapt(libs.hiltCompiler)
    kapt(libs.hiltCompilerKapt)
    implementation(libs.hiltNavigation)

    //glide
    implementation(libs.glide)

    //lottie
    implementation(libs.lottie)
}
kapt{
    correctErrorTypes = true

}