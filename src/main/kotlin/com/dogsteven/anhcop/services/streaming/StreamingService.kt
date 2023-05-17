package com.dogsteven.anhcop.services.streaming

interface StreamingService {
    fun send(key: String, obj: Any)

    fun broadcast(obj: Any)
}