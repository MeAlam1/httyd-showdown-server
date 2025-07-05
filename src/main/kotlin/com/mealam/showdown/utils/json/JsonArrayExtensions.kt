package com.mealam.showdown.utils.json

import com.google.gson.*
import it.unimi.dsi.fastutil.objects.ObjectArrayList

fun JsonArray?.toFloatList(): List<Float> =
    this?.map { it.asFloat } ?: emptyList()

fun JsonArray?.toDoubleList(): List<Double> =
    this?.map { it.asDouble } ?: emptyList()

fun JsonArray?.toIntList(): List<Int> =
    this?.map { it.asInt } ?: emptyList()

fun JsonArray?.toStringList(): List<String> =
    this?.map { it.asString } ?: emptyList()

fun <T> JsonArray?.toObjectList(pContext: JsonDeserializationContext, pClazz: Class<T>): List<T> =
    this?.map { pContext.deserialize<T>(it, pClazz) } ?: emptyList()

fun <T> JsonArray?.toList(pTransform: (JsonElement) -> T): List<T> =
    this?.map(pTransform) ?: ObjectArrayList()