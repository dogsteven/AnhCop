package com.dogsteven.anhcop.controllers

import com.dogsteven.anhcop.services.vendor.VendorCommand
import com.dogsteven.anhcop.services.vendor.VendorService
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

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