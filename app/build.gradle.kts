plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.teladelogin"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.teladelogin"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Firebase
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-firestore-ktx:24.10.3")
    implementation("com.google.firebase:firebase-database:20.3.0")

    // Glide e Cropper
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.github.CanHub:Android-Image-Cropper:4.3.2")

    // SVG e Mapas
    implementation("com.caverock:androidsvg:1.4")
    implementation("org.osmdroid:osmdroid-android:6.1.14")

    // Localização
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Testes instrumentados com JUnit e Espresso
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

// Recomendado para iniciar atividades em testes
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test:runner:1.5.2")

// Testes unitários (já deve ter por padrão)
    testImplementation("junit:junit:4.13.2")

    androidTestImplementation ("androidx.test:core:1.5.0")

    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

    androidTestImplementation ("androidx.test.uiautomator:uiautomator:2.2.0")

}
