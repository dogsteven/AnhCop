package com.dogsteven.anhcop.configurations.firebase

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "firebase")
class FirebaseProperties {
    var googleCredentials: String? = null
}