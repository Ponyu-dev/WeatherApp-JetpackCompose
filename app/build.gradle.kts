import java.io.FileInputStream
import java.util.Properties
import com.github.triplet.gradle.androidpublisher.ReleaseStatus

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    id("kotlin-kapt")

    alias(libs.plugins.playPublisher)
}

// Загрузка keystore.properties
/*val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
} else {
    throw GradleException("Файл keystore.properties не найден!")
}*/
val keystoreProperties = Properties()

// Проверка наличия переменных окружения (для сборки на Jenkins)
val storeFileEnv = System.getenv("STORE_FILE")
val storePasswordEnv = System.getenv("STORE_PASSWORD")
val keyAliasEnv = System.getenv("KEY_ALIAS")
val keyPasswordEnv = System.getenv("KEY_PASSWORD")

if (storeFileEnv != null && storePasswordEnv != null && keyAliasEnv != null && keyPasswordEnv != null) {
    // Если переменные окружения заданы, используем их
    keystoreProperties["keyAlias"] = keyAliasEnv
    keystoreProperties["keyPassword"] = keyPasswordEnv
    keystoreProperties["storeFile"] = storeFileEnv
    keystoreProperties["storePassword"] = storePasswordEnv
} else {
    // Локальная сборка - чтение данных из keystore.properties
    val keystorePropertiesFile = rootProject.file("keystore.properties")
    if (keystorePropertiesFile.exists()) {
        keystoreProperties.load(FileInputStream(keystorePropertiesFile))
    } else {
        throw GradleException("Файл keystore.properties не найден!")
    }
}

// Создание расширения для Play Publisher
open class PlayPublishConfig {
    lateinit var serviceAccountCredentials: File
    lateinit var track: String
    lateinit var releaseName: String
    lateinit var releaseStatus: ReleaseStatus
}

val playPublishConfig = extensions.create<PlayPublishConfig>("playPublishConfig")

// Настройка расширения
playPublishConfig.apply {
    serviceAccountCredentials = rootProject.file("service-account.json")
    track = System.getenv("PLAY_TRACK") ?: "internal" // internal, alpha, beta, production
    releaseName = System.getenv("PLAY_RELEASE_NAME") ?: "Default Release Name"

    // Обработка значения releaseStatus
    val status = System.getenv("PLAY_RELEASE_STATUS") ?: "DRAFT" // For internal only use DRAFT
    releaseStatus = when (status.uppercase()) {
        "DRAFT" -> ReleaseStatus.DRAFT
        "IN_PROGRESS" -> ReleaseStatus.IN_PROGRESS
        "COMPLETED" -> ReleaseStatus.COMPLETED
        else -> ReleaseStatus.DRAFT // По умолчанию
    }
    //userFraction.set(1.0) // do not use for internal
}

android {
    namespace = "com.ponyu.weather.application"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ponyu.weather.application"
        minSdk = 24
        targetSdk = 34
        versionCode = 3
        versionName = "0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = rootProject.file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    play {
        // Overrides defaults
        serviceAccountCredentials.set(playPublishConfig.serviceAccountCredentials)
        track.set(playPublishConfig.track)
        releaseName.set(playPublishConfig.releaseName)
        releaseStatus.set(playPublishConfig.releaseStatus)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.designsystem)
    implementation(projects.core)
    implementation(projects.domain)
    implementation(projects.data)
    implementation(projects.di)
    implementation(projects.navigation)

    implementation(projects.feature.splash)
    implementation(projects.feature.home)
    implementation(projects.feature.forecast)
    implementation(projects.feature.favorites)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.navigation)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    //Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Retrofit
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit)

    // Location
    implementation(libs.gms.play.services.location)

    // Room
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}