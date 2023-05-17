package com.dogsteven.anhcop

import com.dogsteven.anhcop.configurations.firebase.FirebaseProperties
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.net.URI
import java.nio.file.Paths
import kotlin.io.path.absolutePathString

@SpringBootTest
class AnhCopApplicationTests {
    private val logger = LoggerFactory.getLogger(AnhCopApplicationTests::class.java)

    @Autowired
    private lateinit var firebaseProperties: FirebaseProperties

    @Test
    fun testPaths() {
        logger.info(firebaseProperties.googleCredentials)
    }
}
