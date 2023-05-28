package com.dogsteven.anhcop.services.image

interface ImageService {
    fun execute(command: ImageCommand.Load): ImageCommand.Load.Response

    fun execute(command: ImageCommand.Store): ImageCommand.Store.Response

    fun execute(command: ImageCommand.Remove): ImageCommand.Remove.Response
}