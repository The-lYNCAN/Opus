package com.lyncan.opus.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale


object timestampSerializer: KSerializer<Timestamp>{
    private val formatters = listOf(
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()),
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSXXX", Locale.getDefault()),
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX", Locale.getDefault()),
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSXXX", Locale.getDefault()),
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSXXX", Locale.getDefault()),
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault()),
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSXXX", Locale.getDefault()),
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SXXX", Locale.getDefault()),
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
    )
    override val descriptor = PrimitiveSerialDescriptor("Timestamp", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): Timestamp {
        val dateString = decoder.decodeString()
        for (formatter in formatters) {
            try {
                val date = formatter.parse(dateString)
                return Timestamp(date.time)
            } catch (e: Exception) {
                // Try next format
            }
        }
        throw IllegalArgumentException("Unparseable date: \"$dateString\"")
    }
    override fun serialize(encoder: Encoder, value: Timestamp) {
        // Format to yyyy-mm-dd hh:mm:ss[.fffffffff]
        val formattedString = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSSSSS", Locale.getDefault()).format(value)
        encoder.encodeString(formattedString)
    }
}

@Serializable
data class Uploads(
    val upload_id: Int? = null,
    val assignment_id: Int,
    val user_id: String,
    val views: Int,
    val upload_url: String,
    val mark_as_solution: Boolean,
    @Serializable(with = timestampSerializer::class)
    val created_at: Timestamp? = null
)

