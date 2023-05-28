package com.dogsteven.anhcop.services.image

import java.io.InputStream

object ImageCommand {
    class Load(
        val fileName: String
    ) {
        class Response(
            val fileStream: InputStream
        )
    }

    class Store(
        val fileName: String,
        val fileStream: InputStream
    ) {
        object Response
    }

    class Remove(
        val fileName: String
    ) {
        object Response
    }
}