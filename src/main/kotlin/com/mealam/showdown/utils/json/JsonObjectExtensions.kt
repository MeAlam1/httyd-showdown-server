package com.mealam.showdown.utils.json

import com.google.gson.*
import java.math.BigDecimal
import java.math.BigInteger

fun JsonObject?.getOptionalLong(pName: String): Long? =
    this?.takeIf { it.has(pName) }?.let { this.getAsLong(pName) }

fun JsonObject?.getOptionalBoolean(pName: String): Boolean? =
    this?.takeIf { it.has(pName) }?.let { this.getAsBoolean(pName) }

fun JsonObject?.getOptionalFloat(pName: String): Float? =
    this?.takeIf { it.has(pName) }?.let { this.getAsFloat(pName) }

fun JsonObject?.getOptionalDouble(pName: String): Double? =
    this?.takeIf { it.has(pName) }?.let { this.getAsDouble(pName) }

fun JsonObject?.getOptionalInteger(pName: String): Int? =
    this?.takeIf { it.has(pName) }?.let { this.getAsInt(pName) }

fun JsonObject?.getOptionalString(pName: String): String? =
    this?.takeIf { it.has(pName) }?.let { this.getAsString(pName) }

fun <T> JsonObject?.getOptionalObject(pName: String, context: JsonDeserializationContext, type: Class<T>): T? =
    this?.takeIf { it.has(pName) }?.let { this.getAsObject(pName, context, type) }

fun JsonObject?.getOptionalJsonArray(pName: String): JsonArray? =
    this?.takeIf { it.has(pName) }?.let { this.getAsJsonArrayKt(pName) }

fun <T> JsonObject.toMap(pContext: JsonDeserializationContext, pClazz: Class<T>): Map<String, T> =
    entrySet().associate { it.key to pContext.deserialize<T>(it.value, pClazz) }

fun <T> JsonObject.toListMap(pContext: JsonDeserializationContext, pClazz: Class<T>): Map<String, List<T>> =
    entrySet().associate { it.key to it.value.asJsonArray.toObjectList(pContext, pClazz) }

fun JsonObject?.isStringValue(pMemberName: String): Boolean =
    this?.get(pMemberName)?.isJsonPrimitive == true && this.getAsJsonPrimitive(pMemberName).isString

fun JsonObject?.isNumberValue(pMemberName: String): Boolean =
    this?.get(pMemberName)?.isJsonPrimitive == true && this.getAsJsonPrimitive(pMemberName).isNumber

fun JsonObject?.isBooleanValue(pMemberName: String): Boolean =
    this?.get(pMemberName)?.isJsonPrimitive == true && this.getAsJsonPrimitive(pMemberName).isBoolean

fun JsonObject?.isArrayNode(pMemberName: String): Boolean =
    this?.get(pMemberName)?.isJsonArray == true

fun JsonObject?.isObjectNode(pMemberName: String): Boolean =
    this?.get(pMemberName)?.isJsonObject == true

fun JsonObject?.isValidPrimitive(pMemberName: String): Boolean =
    this?.get(pMemberName)?.isJsonPrimitive == true

fun JsonObject?.isValidNode(pMemberName: String): Boolean =
    this != null && this.get(pMemberName) != null

fun JsonObject.getNonNull(pMemberName: String): JsonElement =
    this.get(pMemberName)?.takeIf { !it.isJsonNull }
        ?: throw JsonSyntaxException("Missing field $pMemberName")

fun JsonObject.getAsString(pMemberName: String): String =
    this.get(pMemberName)?.convertToString(pMemberName)
        ?: throw JsonSyntaxException("Missing $pMemberName, expected to find a string")

fun JsonObject.getAsString(pMemberName: String, pFallback: String?): String? =
    if (this.has(pMemberName)) this.getAsString(pMemberName) else pFallback

fun JsonObject.getAsBoolean(pMemberName: String): Boolean =
    this.get(pMemberName)?.convertToBoolean(pMemberName)
        ?: throw JsonSyntaxException("Missing $pMemberName, expected to find a Boolean")

fun JsonObject.getAsBoolean(pMemberName: String, pFallback: Boolean): Boolean =
    if (this.has(pMemberName)) this.getAsBoolean(pMemberName) else pFallback

fun JsonObject.getAsDouble(pMemberName: String): Double =
    this.get(pMemberName)?.convertToDouble(pMemberName)
        ?: throw JsonSyntaxException("Missing $pMemberName, expected to find a Double")

fun JsonObject.getAsDouble(pMemberName: String, pFallback: Double): Double =
    if (this.has(pMemberName)) this.getAsDouble(pMemberName) else pFallback

fun JsonObject.getAsInt(pMemberName: String): Int =
    this.get(pMemberName)?.convertToInt(pMemberName)
        ?: throw JsonSyntaxException("Missing $pMemberName, expected to find an Int")

fun JsonObject.getAsInt(pMemberName: String, pFallback: Int): Int =
    if (this.has(pMemberName)) this.getAsInt(pMemberName) else pFallback

fun JsonObject.getAsLong(pMemberName: String): Long =
    this.get(pMemberName)?.convertToLong(pMemberName)
        ?: throw JsonSyntaxException("Missing $pMemberName, expected to find a Long")

fun JsonObject.getAsLong(pMemberName: String, pFallback: Long): Long =
    if (this.has(pMemberName)) this.getAsLong(pMemberName) else pFallback

fun JsonObject.getAsFloat(pMemberName: String): Float =
    this.get(pMemberName)?.convertToFloat(pMemberName)
        ?: throw JsonSyntaxException("Missing $pMemberName, expected to find a Float")

fun JsonObject.getAsFloat(pMemberName: String, pFallback: Float): Float =
    if (this.has(pMemberName)) this.getAsFloat(pMemberName) else pFallback

fun JsonObject.getAsBigDecimal(pMemberName: String): BigDecimal =
    this.get(pMemberName)?.convertToBigDecimal(pMemberName)
        ?: throw JsonSyntaxException("Missing $pMemberName, expected to find a BigDecimal")

fun JsonObject.getAsBigDecimal(pMemberName: String, pFallback: BigDecimal): BigDecimal =
    if (this.has(pMemberName)) this.getAsBigDecimal(pMemberName) else pFallback

fun JsonObject.getAsBigInteger(pMemberName: String): BigInteger =
    this.get(pMemberName)?.convertToBigInteger(pMemberName)
        ?: throw JsonSyntaxException("Missing $pMemberName, expected to find a BigInteger")

fun JsonObject.getAsBigInteger(pMemberName: String, pFallback: BigInteger): BigInteger =
    if (this.has(pMemberName)) this.getAsBigInteger(pMemberName) else pFallback

fun JsonObject.getAsJsonObjectKt(pMemberName: String): JsonObject =
    this.get(pMemberName)?.convertToJsonObject(pMemberName)
        ?: throw JsonSyntaxException("Missing $pMemberName, expected to find a JsonObject")

fun JsonObject.getAsJsonObject(pMemberName: String, pFallback: JsonObject?): JsonObject? =
    if (this.has(pMemberName)) this.getAsJsonObject(pMemberName) else pFallback

fun JsonObject.getAsJsonArrayKt(pMemberName: String): JsonArray =
    this.get(pMemberName)?.convertToJsonArray(pMemberName)
        ?: throw JsonSyntaxException("Missing $pMemberName, expected to find a JsonArray")

fun JsonObject.getAsJsonArray(pMemberName: String, pFallback: JsonArray?): JsonArray? =
    if (this.has(pMemberName)) this.getAsJsonArray(pMemberName) else pFallback

fun <T> JsonElement?.convertToObject(pMemberName: String, pContext: JsonDeserializationContext, pType: Class<T>): T {
    return this?.let { pContext.deserialize<T>(it, pType) }
        ?: throw JsonSyntaxException("Missing $pMemberName")
}

fun <T> JsonObject.getAsObject(pMemberName: String, pContext: JsonDeserializationContext, pType: Class<T>): T {
    return if (this.has(pMemberName)) {
        this.get(pMemberName).convertToObject(pMemberName, pContext, pType)
    } else {
        throw JsonSyntaxException("Missing $pMemberName")
    }
}

fun <T> JsonObject.getAsObject(
    pMemberName: String,
    pFallback: T?,
    pContext: JsonDeserializationContext,
    pType: Class<T>
): T? {
    return if (this.has(pMemberName)) {
        this.get(pMemberName).convertToObject(pMemberName, pContext, pType)
    } else {
        pFallback
    }
}