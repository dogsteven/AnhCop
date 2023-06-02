package com.dogsteven.anhcop.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/api/test")
class TestController {
    data class TestResponse(
        val instant: Instant
    )

    @GetMapping("/{instant}")
    @ResponseBody
    fun testInstantParameter(@PathVariable("instant") instant: Instant): TestResponse {
        return TestResponse(instant)
    }
}