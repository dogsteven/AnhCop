package com.dogsteven.anhcop

import com.dogsteven.anhcop.utils.PositiveElements
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest

class TestModel(
    @field:PositiveElements(message = "Positive elements error")
    val test: Set<Int>
)

@SpringBootTest
class AnhCopApplicationTests {
    private val logger = LoggerFactory.getLogger(AnhCopApplicationTests::class.java)

    @Test
    fun testPaths() {
    }
}