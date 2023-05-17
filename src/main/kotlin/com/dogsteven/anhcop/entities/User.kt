package com.dogsteven.anhcop.entities

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "t_user")
@Inheritance(strategy = InheritanceType.JOINED)
open class User(
    @Id
    @GeneratedValue
    open var id: Long? = null,
    @Column
    open var enabled: Boolean = true,
    @Column
    open var username: String,
    @Column
    open var password: String,
    @Column
    open var name: String,
) {
    open val role: String
        @Transient
        get() = "ROLE_USER"

    val principal: Principal
        @Transient
        get() = Principal(this)

    open val model: Model
        @Transient
        get() = Model(
            id = id ?: throw RuntimeException("Field 'User::id' is null"),
            username = username,
            password = password,
            name = name
        )

    @Entity
    @Table(name = "t_administrator")
    class Administrator(
        id: Long? = null,
        enabled: Boolean = true,
        username: String,
        password: String,
        name: String
    ): User(id, enabled, username, password, name) {
        override val role: String
            @Transient
            get() = "ROLE_ADMINISTRATOR"

        override val model: User.Model
            @Transient
            get() = Model(
                id = id ?: throw RuntimeException("Field 'Administrator::id' is null"),
                username = username,
                password = password,
                name = name
            )

        class Model(
            id: Long,
            username: String,
            password: String,
            name: String
        ): User.Model(id, username, password, name)
    }

    @Entity
    @Table(name = "t_employee")
    class Employee(
        id: Long? = null,
        enabled: Boolean = true,
        username: String,
        password: String,
        name: String,
        @Column
        var phone: String,
        @ManyToOne(fetch = FetchType.LAZY, optional = true)
        @JoinColumn(name = "working_vendor_id")
        var workingVendor: Vendor? = null
    ): User(id, enabled, username, password, name) {
        override val role: String
            @Transient
            get() = "ROLE_EMPLOYEE"

        override val model: User.Model
            @Transient
            get() = Model(
                id = id ?: throw RuntimeException("Field 'Employee::id' is null"),
                username = username,
                password = password,
                name = name,
                phone = phone,
                workingVendorId = if (workingVendor == null) {
                    null
                } else {
                    workingVendor?.id ?: throw RuntimeException("Field 'Employee:workingVendor::id' is null")
                }
            )

        class Model(
            id: Long,
            username: String,
            password: String,
            name: String,
            phone: String,
            workingVendorId: Long?
        ): User.Model(id, username, password, name)
    }

    @Entity
    @Table(name = "t_customer")
    class Customer(
        id: Long? = null,
        enabled: Boolean = true,
        username: String,
        password: String,
        name: String,
        @Column
        var phone: String
    ): User(id, enabled, username, password, name) {
        override val role: String
            @Transient
            get() = "ROLE_CUSTOMER"

        override val model: User.Model
            @Transient
            get() = Model(
                id = id ?: throw RuntimeException("Field 'Customer::id' is null"),
                username = username,
                password = password,
                name = name,
                phone = phone
            )

        class Model(
            id: Long,
            username: String,
            password: String,
            name: String,
            phone: String
        ): User.Model(id, username, password, name)
    }

    open class Model(
        val id: Long,
        val username: String,
        val password: String,
        val name: String
    )

    class Principal(val user: User): UserDetails {
        override fun getUsername(): String = user.username

        override fun getPassword(): String = user.password

        override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(
            SimpleGrantedAuthority(user.role)
        )

        override fun isEnabled(): Boolean = user.enabled

        override fun isAccountNonExpired(): Boolean = true

        override fun isAccountNonLocked(): Boolean = true

        override fun isCredentialsNonExpired(): Boolean = true
    }
}