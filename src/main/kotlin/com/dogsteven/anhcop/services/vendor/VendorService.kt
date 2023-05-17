package com.dogsteven.anhcop.services.vendor

import org.springframework.transaction.annotation.Transactional

interface VendorService {
    @Transactional
    fun execute(command: VendorCommand.GetAllVendors): VendorCommand.GetAllVendors.Response

    @Transactional
    fun execute(command: VendorCommand.CreateVendor): VendorCommand.CreateVendor.Response

    @Transactional
    fun execute(command: VendorCommand.UpdateVendor): VendorCommand.UpdateVendor.Response

    @Transactional
    fun execute(command: VendorCommand.DeleteVendor): VendorCommand.DeleteVendor.Response
}