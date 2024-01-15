plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id("com.google.gms.google-services")
}


android {
    namespace = "com.example.browserapp"
    compileSdk = 34
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        applicationId = "com.example.browserapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField ("String", "SUBSCRIPTION_KEY", "\"fbf107a7b57f4aa1b014d263a1588d9d\"")
    }


    buildFeatures {
        viewBinding = true
        dataBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
//    configurations.all {
//        exclude("com.google.protobuf", "protobuf-java")
//        exclude("com.sun.activation","javax.activation")
//    }
//    implementation ("com.android.tools.build:gradle:4.1.0")


    implementation ("com.airbnb.android:lottie:6.3.0")
    implementation ("org.threeten:threetenbp:1.6.0")
    implementation ("androidx.fragment:fragment-ktx:1.4.1")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("dev.icerock.moko:resources:0.20.1") // Or a newer version
    implementation ("io.github.cdimascio:dotenv-kotlin:6.2.2") // Or a newer version
    implementation ("com.google.code.gson:gson:2.8.5")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.paging:paging-common-android:3.3.0-alpha02")
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    implementation(platform("com.google.firebase:firebase-bom:32.2.3"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-auth") // Use the latest version

//    implementation ("com.google.android.gms:play-services-auth:22.0.0")
    implementation ("com.google.firebase:firebase-firestore") // Use the latest version

}