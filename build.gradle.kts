buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(GradleDependency.ANDROID_GRADLE)
        classpath(GradleDependency.KOTLIN)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
