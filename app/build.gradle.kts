plugins {
    id(Plugins.androidApplication)
    id(Plugins.kotlinAndroid)
    id(Plugins.kotlinKapt)
    id(Plugins.daggerHiltAndroid)
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
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules-debug.pro"
            )
        }
    }
}

dependencies {
    implementation(AndroidX.coreKtx)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.fragment)
    implementation(AndroidX.activity)
    implementation(AndroidX.constraintLayout)
    implementation(Leanback.leanback)
    implementation(Glide.glide)

    implementation(DaggerHilt.hiltAndroid)
    kapt(DaggerHilt.hiltAndroidCompiler)

    implementation(project(Modules.core))
    implementation(project(Modules.coreUi))
    implementation(project(Modules.photosUi))
    implementation(project(Modules.photosDomain))
    implementation(project(Modules.photosData))

    implementation(Retrofit.okHttpBmo)
    implementation(Retrofit.okHttp)
    implementation(Retrofit.okHttpLoggingInterceptor)
    implementation(Retrofit.retrofit)
    implementation(Retrofit.gsonConverter)

    kapt(Room.roomCompiler)
    implementation(Room.roomKtx)
    implementation(Room.roomRuntime)

    implementation(Fragment.navigationFragment)
    implementation(Fragment.navigationUi)

}