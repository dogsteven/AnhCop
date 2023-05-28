package com.dogsteven.anhcop.controllers

import com.dogsteven.anhcop.services.vendor.VendorCommand
import com.dogsteven.anhcop.services.vendor.VendorService
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/vendor")
class VendorController(
    private val vendorService: VendorService
) {
    @GetMapping
    @ResponseBody
    fun getAllVendors(): VendorCommand.GetAllVendors.Response {
        val command = VendorCommand.GetAllVendors

        return vendorService.execute(command)
    }

    @PostMapping
    @ResponseBody
    @Secured("ROLE_ADMINISTRATOR")
    fun createVendor(@RequestBody command: VendorCommand.CreateVendor): VendorCommand.CreateVendor.Response {
        return vendorService.execute(command)
    }

    @PutMapping
    @ResponseBody
    @Secured("ROLE_ADMINISTRATOR")
    fun updateVendor(@RequestBody command: VendorCommand.UpdateVendor): VendorCommand.UpdateVendor.Response {
        return vendorService.execute(command)
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @Secured("ROLE_ADMINISTRATOR")
    fun deleteVendor(@PathVariable("id") id: Long): VendorCommand.DeleteVendor.Response {
        val command = VendorCommand.DeleteVendor(id)

        return vendorService.execute(command)
    }
}