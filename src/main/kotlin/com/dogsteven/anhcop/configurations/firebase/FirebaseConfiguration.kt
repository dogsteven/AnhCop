package com.dogsteven.anhcop.configurations.firebase

import com.dogsteven.anhcop.configurations.anhcop.AnhCopProperties
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.io.path.Path
import kotlin.io.path.inputStream

@Configuration
class FirebaseConfiguration {
    @Bean
    fun googleCredentials(
        anhCopProperties: AnhCopProperties
    ): GoogleCredentials {
        val inputStream = Path(anhCopProperties.firebase.googleCredentials).inputStream()
        return GoogleCredentials.fromStream(inputStream)
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