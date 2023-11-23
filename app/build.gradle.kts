plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.gmsService)
    alias(libs.plugins.kotlinKsp)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.parcelize)


}

android {
    signingConfigs {
        create("release") {
            storeFile = file("/home/jasi/Desktop/worldNow/releases.jks")
            storePassword = "jasmeet34"
            keyAlias = "key0"
            keyPassword = "jasmeet34"
        }
    }
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
            isDebuggable = true
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
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
        kotlinCompilerExtensionVersion = "1.5.3"
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
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    //material Icons
    implementation(libs.materialIcons)

    //firebase
    implementation(platform(libs.firebaseBom))
    implementation(libs.firebaseAuth)
    implementation(libs.firebaseFirestore)
    implementation(libs.play.services.auth)
    implementation(libs.firebaseStorage)

    //viewModel Compose
    implementation(libs.viewModel)
    implementation(libs.liveData)

    //hilt
    implementation(libs.hiltAndroid)
    ksp(libs.hiltCompiler)
    ksp(libs.hiltCompilerKapt)
    implementation(libs.hiltNavigation)

    //glide
    implementation(libs.glide)

    //lottie
    implementation(libs.lottie)

    //system ui controller
    implementation(libs.system.ui.controller)

    implementation(libs.coil.compose)

    //room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.liveData)
    ksp(libs.rooom.compiler)

    //gson
    implementation(libs.gson)

    //constraint layout
    implementation(libs.constraintlayout)

    implementation (libs.activity.ktx)

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofitGson)

    //custom chrome tab
    implementation(libs.chromeCustomTabs)


    implementation (libs.runtime)

    //Junit-4
    testImplementation(libs.junit)

    implementation (libs.threetenabp)

    //dataStore
    implementation (libs.datastore.preferences)


}
