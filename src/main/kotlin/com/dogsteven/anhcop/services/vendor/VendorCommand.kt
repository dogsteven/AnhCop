package com.dogsteven.anhcop.services.vendor

import com.dogsteven.anhcop.entities.Vendor

object VendorCommand {
    object GetAllVendors {
        class Response(
            val vendors: Collection<Vendor.Model>
        )
    }

    class CreateVendor(
        val name: String
    ) {
        class Response(
            val id: Long
        )
    }

    class UpdateVendor(
        val id: Long,
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