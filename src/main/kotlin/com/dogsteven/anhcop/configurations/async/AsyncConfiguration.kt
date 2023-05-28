package com.dogsteven.anhcop.configurations.async

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Configuration
@EnableAsync
class AsyncConfiguration: AsyncConfigurer {
    @Bean
    override fun getAsyncExecutor(): Executor {
        return Executors.newCachedThreadPool()
    }

    @Bean
    fun webMvcConfigurer(): WebMvcConfigurer {
        return object: WebMvcConfigurer {
            override fun configureAsyncSupport(configurer: AsyncSupportConfigurer) {
                Executors.newSingleThreadExecutor()
                    .run(::ConcurrentTaskExecutor)
                    .run(configurer::setTaskExecutor)
            }
        }
    }
}