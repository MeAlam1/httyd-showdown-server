package com.mealam.showdown.utils.json

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.JsonWriter
import org.apache.commons.lang3.StringUtils
import java.io.IOException
import java.io.StringWriter
import java.math.BigDecimal
import java.math.BigInteger

fun JsonElement.convertToJsonArray(pMemberName: String): JsonArray =
    if (isJsonArray) asJsonArray
    else throw JsonSyntaxException("Expected $pMemberName to be a JsonArray, was $this")

fun JsonElement.convertToJsonObject(pMemberName: String): JsonObject =
    if (isJsonObject) asJsonObject
    else throw JsonSyntaxException("Expected $pMemberName to be a JsonObject, was $this")

fun JsonElement.convertToBigInteger(pMemberName: String): BigInteger =
    if (isJsonPrimitive && asJsonPrimitive.isNumber) asBigInteger
    else throw JsonSyntaxException("Expected $pMemberName to be a BigInteger, was $this")

fun JsonElement.convertToBigDecimal(pMemberName: String): BigDecimal =
    if (isJsonPrimitive && asJsonPrimitive.isNumber) asBigDecimal
    else throw JsonSyntaxException("Expected $pMemberName to be a BigDecimal, was $this")

fun JsonElement.convertToString(pMemberName: String): String =
    if (isJsonPrimitive) asString
    else throw JsonSyntaxException("Expected $pMemberName to be a string, was $this")

fun JsonElement.convertToBoolean(pMemberName: String): Boolean =
    if (isJsonPrimitive) asBoolean
    else throw JsonSyntaxException("Expected $pMemberName to be a Boolean, was $this")

fun JsonElement.convertToDouble(pMemberName: String): Double =
    if (isJsonPrimitive && asJsonPrimitive.isNumber) asDouble
    else throw JsonSyntaxException("Expected $pMemberName to be a Double, was $this")

fun JsonElement.convertToInt(pMemberName: String): Int =
    if (isJsonPrimitive && asJsonPrimitive.isNumber) asInt
    else throw JsonSyntaxException("Expected $pMemberName to be an Int, was $this")

fun JsonElement.convertToLong(pMemberName: String): Long =
    if (isJsonPrimitive && asJsonPrimitive.isNumber) asLong
    else throw JsonSyntaxException("Expected $pMemberName to be a Long, was $this")

fun JsonElement.convertToFloat(pMemberName: String): Float =
    if (isJsonPrimitive && asJsonPrimitive.isNumber) asFloat
    else throw JsonSyntaxException("Expected $pMemberName to be a Float, was $this")

fun JsonElement?.getType(): String {
    val s = StringUtils.abbreviateMiddle(this.toString(), "...", 10)
    return when {
        this == null -> "null (missing)"
        this.isJsonNull -> "null (pJson)"
        this.isJsonArray -> "an array ($s)"
        this.isJsonObject -> "an object ($s)"
        this.isJsonPrimitive -> {
            val jsonPrimitive = this.asJsonPrimitive
            when {
                jsonPrimitive.isNumber -> "a number ($s)"
                jsonPrimitive.isBoolean -> "a boolean ($s)"
                else -> s
            }
        }

        else -> s
    }
}

fun JsonElement.toStableString(): String {
    val stringWriter = StringWriter()
    val jsonWriter = JsonWriter(stringWriter)
    try {
        this.writeValue(jsonWriter, Comparator.naturalOrder())
    } catch (e: IOException) {
        throw AssertionError(e)
    }
    return stringWriter.toString()
}

@Throws(IOException::class)
fun JsonElement?.writeValue(pWriter: JsonWriter, pSorter: Comparator<String>? = null) {
    if (this != null && !this.isJsonNull) {
        when {
            this.isJsonPrimitive -> {
                val jsonPrimitive = this.asJsonPrimitive
                when {
                    jsonPrimitive.isNumber -> pWriter.value(jsonPrimitive.asNumber)
                    jsonPrimitive.isBoolean -> pWriter.value(jsonPrimitive.asBoolean)
                    else -> pWriter.value(jsonPrimitive.asString)
                }
            }

            this.isJsonArray -> {
                pWriter.beginArray()
                for (element in this.asJsonArray) {
                    element.writeValue(pWriter, pSorter)
                }
                pWriter.endArray()
            }

            this.isJsonObject -> {
                pWriter.beginObject()
                val entries = if (pSorter != null) {
                    this.asJsonObject.entrySet().sortedWith(compareBy(pSorter) { it.key })
                } else {
                    this.asJsonObject.entrySet()
                }
                for (entry in entries) {
                    pWriter.name(entry.key)
                    entry.value.writeValue(pWriter, pSorter)
                }
                pWriter.endObject()
            }

            else -> throw IllegalArgumentException("Couldn't write ${this.javaClass}")
        }
    } else {
        pWriter.nullValue()
    }
}

fun JsonElement.isStringValue(): Boolean =
    isJsonPrimitive && asJsonPrimitive.isString

fun JsonElement.isNumberValue(): Boolean =
    isJsonPrimitive && asJsonPrimitive.isNumber

fun JsonElement.isBooleanValue(): Boolean =
    isJsonPrimitive && asJsonPrimitive.isBoolean