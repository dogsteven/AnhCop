package com.dogsteven.anhcop.utils

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

object GsonTypeAdapters {
    object OffsetDateTimeGsonTypeAdapter: TypeAdapter<OffsetDateTime>() {
        override fun read(`in`: JsonReader): OffsetDateTime {
            val epochSecond = `in`.nextLong()
            return OffsetDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), ZoneOffset.systemDefault())
        }

        override fun write(out: JsonWriter, value: OffsetDateTime) {
            out.value(value.toEpochSecond())
        }
    }
}