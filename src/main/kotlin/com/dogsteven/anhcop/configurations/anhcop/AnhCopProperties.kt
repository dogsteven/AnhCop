package com.dogsteven.anhcop.configurations.anhcop

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan

@ConfigurationProperties(prefix = "anhcop")
class AnhCopProperties(
    val jwt: JWTProperties,
    val firebase: FirebaseProperties
) {
    class JWTProperties(
        val audience: String,
        val issuer: String,
        val secretKey: String
    )

    class FirebaseProperties(
        val googleCredentials: String
    )
}