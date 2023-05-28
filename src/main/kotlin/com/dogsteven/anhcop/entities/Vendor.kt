package com.dogsteven.anhcop.entities

import jakarta.persistence.*

@Entity
@Table(name = "t_vendors")
class Vendor(
    @Id
    @GeneratedValue
    var id: Long? = null,
    @Column(unique = true)
    var name: String
) {
    val model: Model
        @Transient
        get() = Model(
            id = id ?: throw RuntimeException("Field 'Vendor::id' is null"),
            name = name
        )

    class Model(
        val id: Long,
        val name: String
    )
}