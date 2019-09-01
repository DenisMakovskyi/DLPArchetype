plugins {
    id(GradlePluginId.ANDROID_LIBRARY)
    id(GradlePluginId.KOTLIN_ANDROID)
    id(GradlePluginId.KOTLIN_ANDROID_EXTENSIONS)
    id(GradlePluginId.KOTLIN_KAPT)
}

android {
    compileSdkVersion(AndroidConfig.COMPILE_SDK_VERSION)
    buildToolsVersion(AndroidConfig.BUILD_TOOLS_VERSION)

    defaultConfig {
        minSdkVersion(AndroidConfig.MIN_SDK_VERSION)
        targetSdkVersion(AndroidConfig.TARGET_SDK_VERSION)
        versionCode = AndroidConfig.VERSION_CODE
        versionName = AndroidConfig.VERSION_NAME
        testInstrumentationRunner = AndroidConfig.TEST_INSTRUMENTATION_RUNNER
    }
    buildTypes {
        getByName(BuildType.RELEASE) {
            isMinifyEnabled = BuildTypeRelease.isMinifyEnabled
            proguardFiles("proguard-android-optimize.txt", "proguard-rules.pro")

        }
    }
    dataBinding {
        isEnabled = true
    }
    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // Kotlin
    implementation(LibraryDependency.CORE_KTX)
    implementation(LibraryDependency.KOTLIN)
    implementation(LibraryDependency.KOTLIN_REFLECT)
    implementation(LibraryDependency.COROUTINES_CORE)
    implementation(LibraryDependency.COROUTINES_ANDROID)
    kapt(LibraryDependency.DATABINDING_COMPILER)

    // AndroidX
    implementation(LibraryDependency.APP_COMPACT)
    implementation(LibraryDependency.MATERIAL)

    // Jetpack
    //-ViewModel and LiveData
    implementation(LibraryDependency.LIFECYCLE_VIEW_MODEL_KTX)
    implementation(LibraryDependency.LIVEDATA)
    kapt(LibraryDependency.LIFECYCLE_COMPILER)
    //-Paging
    implementation(LibraryDependency.PAGING)

    // DI
    implementation(LibraryDependency.DAGGER_ANDROID)
    implementation(LibraryDependency.DAGGER_ANDROID_SUPPORT)
    kapt(LibraryDependency.DAGGER_ANDROID_PROCESSOR)

    // RX
    implementation(LibraryDependency.RX_JAVA)
    implementation(LibraryDependency.RX_ANDROID)
}
