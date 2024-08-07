package de.lucas.cookingrecipes.core.data.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object UrlEncoder : KSerializer<String> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("UrlEncodedString", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) {
        val encoded = URLEncoder.encode(value, StandardCharsets.UTF_8.toString())
        encoder.encodeString(encoded)
    }

    override fun deserialize(decoder: Decoder): String {
        return URLDecoder.decode(decoder.decodeString(), StandardCharsets.UTF_8.toString())
    }
}

object UrlListEncoder : KSerializer<List<String>> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("UrlList") {
        element<String>("elements")
    }

    override fun serialize(encoder: Encoder, value: List<String>) {
        encoder.encodeSerializableValue(
            ListSerializer(String.serializer()),
            value.map { URLEncoder.encode(it, "UTF-8") }
        )
    }

    override fun deserialize(decoder: Decoder): List<String> {
        return decoder.decodeSerializableValue(ListSerializer(String.serializer()))
            .map { URLDecoder.decode(it, "UTF-8") }
    }
}