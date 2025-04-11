plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.tlucontact_canhan"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.tlucontact_canhan"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:9000\"")
    }

    android.buildFeatures.buildConfig = true

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
    viewBinding {
        enable = true
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    implementation ("androidx.cardview:cardview:1.0.0") // Để tạo giao diện đẹp với CardView
    implementation ("com.google.android.material:material:1.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Gọi API bằng Retrofit
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Chuyển đổi JSON -> Object
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")   // LiveData, dùng để quản lý dữ liệu
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3") // Ghi log API
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation ("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2") // Hỗ trợ suspend functions
//    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")   // Coroutine, dùng để gọi API
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
}