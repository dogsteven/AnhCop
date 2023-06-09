package com.dogsteven.anhcop.services.vendor

import com.dogsteven.anhcop.entities.Vendor
import com.dogsteven.anhcop.repositories.EmployeeRepository
import com.dogsteven.anhcop.repositories.VendorRepository
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validator
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class VendorServiceImpl(
    private val vendorRepository: VendorRepository,
    private val employeeRepository: EmployeeRepository,
    private val validator: Validator
): VendorService {
    override fun execute(command: VendorCommand.GetAllVendors): VendorCommand.GetAllVendors.Response {
        val vendors = vendorRepository.findAll().map(Vendor::model)

        return VendorCommand.GetAllVendors.Response(
            vendors = vendors
        )
    }

    override fun execute(command: VendorCommand.CreateVendor): VendorCommand.CreateVendor.Response {
        val violations = validator.validate(command)

        if (violations.isNotEmpty()) {
            throw ConstraintViolationException(violations)
        }

        if (vendorRepository.existsByName(command.name)) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Vendor with name \"${command.name}\" already exists"
            )
        }

        val vendor = Vendor(
            name = command.name
        ).let(vendorRepository::save)

        return VendorCommand.CreateVendor.Response(
            id = vendor.id!!
        )
    }

    override fun execute(command: VendorCommand.UpdateVendor): VendorCommand.UpdateVendor.Response {
        val violations = validator.validate(command)

        if (violations.isNotEmpty()) {
            throw ConstraintViolationException(violations)
        }

        val vendor = vendorRepository.findByIdOrNull(command.id)
            ?: throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Vendor with id \"${command.id}\" does not exist"
            )

        if (command.name != null) {
            if (vendorRepository.existsByName(command.name)) {
                throw ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Vendor with name \"${command.name}\" already exists"
                )
            }
            vendor.name = command.name
        }

        vendorRepository.save(vendor)

        return VendorCommand.UpdateVendor.Response
    }

    override fun execute(command: VendorCommand.DeleteVendor): VendorCommand.DeleteVendor.Response {
        val vendor = vendorRepository.findByIdOrNull(command.id)
            ?: throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Vendor with id \"${command.id}\" does not exist"
            )

        val employees = employeeRepository.findAllByWorkingVendor(vendor)

        for (employee in employees) {
            employee.workingVendor = null
        }

        employeeRepository.saveAll(employees)

        vendorRepository.delete(vendor)

        return VendorCommand.DeleteVendor.Response
    }
}