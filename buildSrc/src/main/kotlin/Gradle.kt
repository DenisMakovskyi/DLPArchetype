object GradlePlugin {
    const val ANDROID_GRADLE            = "3.5.0"
}

object GradlePluginId {
    const val ANDROID_APPLICATION       = "com.android.application"
    const val ANDROID_LIBRARY           = "com.android.library"
    const val KOTLIN_KAPT               = "kotlin-kapt"
    const val KOTLIN_ANDROID            = "kotlin-android"
    const val KOTLIN_ANDROID_EXTENSIONS = "kotlin-android-extensions"
}


object GradleDependency {
    const val KOTLIN                    = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Core.KOTLIN}"
    const val ANDROID_GRADLE            = "com.android.tools.build:gradle:${GradlePlugin.ANDROID_GRADLE}"
}