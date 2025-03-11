import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

fun Project.readProperties(): Properties{
    val properties = Properties()
    val file = file("../local.properties")
    if (file.exists()) {
        properties.load(file.inputStream())
        return properties
    } else {
        throw GradleException("local.properties not found")
    }
}


android {
    namespace = "com.vsv.yandexmapcompose"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.vsv.yandexmapcompose"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "YANDEX_MAPKIT_API_KEY", "\"${System.getenv("YANDEX_MAPKIT_API_KEY")}\"")
    }


    buildTypes {
        val properties = readProperties()
        release {
            buildConfigField("String", "YANDEX_MAPKIT_API_KEY", "\"${properties.getProperty("yandexApiKey")}\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            buildConfigField("String", "YANDEX_MAPKIT_API_KEY", "\"${properties.getProperty("yandexApiKey")}\"")
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
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.yandex.mapkit)

}