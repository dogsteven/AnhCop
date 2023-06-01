package com.dogsteven.anhcop.configurations.anhcop

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(AnhCopProperties::class)
class AnhCopConfiguration