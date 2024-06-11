package org.astu.infrastructure

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

object AnySerializer : KSerializer<Any> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Any")

    override fun serialize(encoder: Encoder, value: Any) {
        when (value) {
            is Int -> encoder.encodeInt(value)
            is Long -> encoder.encodeLong(value)
            is String -> encoder.encodeString(value)
            is Boolean -> encoder.encodeBoolean(value)
            is Double -> encoder.encodeDouble(value)
            is Float -> encoder.encodeFloat(value)
            is JsonObject -> encoder.encodeSerializableValue(JsonObject.serializer(), value)
            is JsonArray -> encoder.encodeSerializableValue(JsonArray.serializer(), value)
            else -> throw SerializationException("Unsupported type: ${value::class}")
        }
    }

    override fun deserialize(decoder: Decoder): Any {
        val input = decoder as? JsonDecoder ?: throw SerializationException("This class can be loaded only by Json")
        val jsonElement = input.decodeJsonElement()
        return when {
            jsonElement is JsonPrimitive && jsonElement.isString -> jsonElement.content
            jsonElement is JsonPrimitive && jsonElement.booleanOrNull != null -> jsonElement.boolean
            jsonElement is JsonPrimitive && jsonElement.longOrNull != null -> jsonElement.long
            jsonElement is JsonPrimitive && jsonElement.doubleOrNull != null -> jsonElement.double
            jsonElement is JsonPrimitive && jsonElement.floatOrNull != null -> jsonElement.float
            jsonElement is JsonObject -> jsonElement
            jsonElement is JsonArray -> jsonElement
            else -> throw SerializationException("Unsupported json element: $jsonElement")
        }
    }
}