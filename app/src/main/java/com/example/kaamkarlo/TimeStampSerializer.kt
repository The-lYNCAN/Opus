package com.example.kaamkarlo


import java.sql.Timestamp
import java.time.LocalDateTime
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

object TimeStampSerializer : KSerializer<Timestamp> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ZonedDateTime", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Timestamp) {
        encoder.encodeString(value.toString())
    }
    override fun deserialize(decoder: Decoder): Timestamp {
        val str = decoder.decodeString()
        val ldt = LocalDateTime.parse(str)
        return Timestamp.valueOf(ldt.toString())
    }
}