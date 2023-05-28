package com.dogsteven.anhcop.services.vendor

import com.dogsteven.anhcop.entities.Vendor
import com.dogsteven.anhcop.utils.NullOrNotBlank
import jakarta.validation.constraints.NotBlank

object VendorCommand {
    object GetAllVendors {
        class Response(
            val vendors: Collection<Vendor.Model>
        )
    }

    class CreateVendor(
        @field:NotBlank(message = "Name must not be blank")
        val name: String
    ) {
        class Response(
            val id: Long
        )
    }

    class UpdateVendor(
        val id: Long,
        @field:NullOrNotBlank(message = "Name must not be blank")
        val name: String?
    ) {
        object Response
    }

    class DeleteVendor(
        val id: Long
    ) {
        object Response
    }
}