package com.dogsteven.anhcop.configurations.database

import com.dogsteven.anhcop.entities.User
import com.dogsteven.anhcop.entities.Vendor
import com.dogsteven.anhcop.repositories.*
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class DatabaseConfiguration {
    private val logger = LoggerFactory.getLogger(DatabaseConfiguration::class.java)

    @Bean
    fun setup(
        vendorRepository: VendorRepository,
        userRepository: UserRepository,
        productRepository: ProductRepository,
        orderRepository: OrderRepository,
        orderItemRepository: OrderItemRepository,
        passwordEncoder: PasswordEncoder
    ): CommandLineRunner = CommandLineRunner { _ ->
        if (!userRepository.existsByUsername("administrator")) {
            User.Administrator(
                username = "administrator",
                password = "admin123admin".let(passwordEncoder::encode),
                name = "Loyal Administrator"
            ).apply(userRepository::save)
        }

        if (!userRepository.existsByUsername("employee1")) {
            val mainVendor = vendorRepository.findByName("Chi nhánh chính, thôn Tân Thành")
                ?: Vendor(name = "Chi nhánh chính, thôn Tân Thành").let(vendorRepository::save)

            User.Employee(
                username = "employee1",
                password = "employee123employee".let(passwordEncoder::encode),
                name = "Loyal Employee",
                phone = "0123456789",
                workingVendor = mainVendor
            ).apply(userRepository::save)
        }
    }
}