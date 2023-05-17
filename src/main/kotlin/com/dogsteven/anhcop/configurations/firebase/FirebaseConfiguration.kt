package com.dogsteven.anhcop.configurations.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.io.path.Path
import kotlin.io.path.inputStream

@Configuration
class FirebaseConfiguration(
    private val firebaseProperties: FirebaseProperties
) {

    @Bean
    fun googleCredentials(): GoogleCredentials {
        return if (firebaseProperties.googleCredentials != null) {
            val inputStream = Path(firebaseProperties.googleCredentials!!).inputStream()
            GoogleCredentials.fromStream(inputStream)
        } else {
            GoogleCredentials.getApplicationDefault()
        }
    }

    @Bean
    fun firebaseApp(credentials: GoogleCredentials): FirebaseApp {
        val options = FirebaseOptions.builder()
            .setCredentials(credentials)
            .build()

        return FirebaseApp.initializeApp(options)
    }

    @Bean
    fun firebaseMessaging(firebaseApp: FirebaseApp): FirebaseMessaging {
        return FirebaseMessaging.getInstance(firebaseApp)
    }
}