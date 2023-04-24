plugins {
    id (Plugins.androidApplication)
    id (Plugins.kotlinAndroid)
    id (Plugins.kotlinKapt)
    id (Plugins.daggerHiltAndroid)
}

android {
    namespace = ProjectConfig.appId
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = ProjectConfig.appId
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules-debug.pro")
        }
    }
}

dependencies {
    implementation(AndroidX.coreKtx)
    implementation(Leanback.leanback)
    implementation ("com.github.bumptech.glide:glide:4.11.0")

    implementation(DaggerHilt.hiltAndroid)
    kapt(DaggerHilt.hiltAndroidCompiler)

    implementation (project(Modules.photos_ui))
}