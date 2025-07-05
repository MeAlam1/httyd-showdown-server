package com.mealam.showdown.utils.json

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import java.io.Reader

private val sharedGson = Gson()

private fun String.toJsonReader(pLenient: Boolean = false): JsonReader =
    JsonReader(this.reader()).apply { isLenient = pLenient }

private fun Reader.toJsonReader(pLenient: Boolean = false): JsonReader =
    JsonReader(this).apply { isLenient = pLenient }

fun Gson.parseObject(pJson: String, pLenient: Boolean = false): JsonObject =
    fromJson(pJson.toJsonReader(pLenient), JsonObject::class.java)

fun Gson.parseObject(pReader: Reader, pLenient: Boolean = false): JsonObject =
    fromJson(pReader.toJsonReader(pLenient), JsonObject::class.java)

fun Gson.parseArray(pJson: String): JsonArray =
    fromJson(pJson, JsonArray::class.java)

fun Gson.parseArray(pReader: Reader): JsonArray =
    fromJson(pReader, JsonArray::class.java)

fun <T> Gson.fromJson(pJson: String, pClazz: Class<T>, pLenient: Boolean = false): T =
    fromJson(pJson.toJsonReader(pLenient), pClazz)

fun <T> Gson.fromJson(pReader: Reader, pClazz: Class<T>, pLenient: Boolean = false): T =
    fromJson(pReader.toJsonReader(pLenient), pClazz)

fun <T> Gson.fromJson(pJson: String, pType: TypeToken<T>, pLenient: Boolean = false): T? =
    fromJson<T>(pJson.toJsonReader(pLenient), pType.type)

fun <T> Gson.fromJson(pReader: Reader, pType: TypeToken<T>, pLenient: Boolean = false): T? =
    fromJson<T>(pReader.toJsonReader(pLenient), pType.type)

fun String.parseJsonObject(pLenient: Boolean = false): JsonObject =
    sharedGson.parseObject(this, pLenient)

fun Reader.parseJsonObject(pLenient: Boolean = false): JsonObject =
    sharedGson.parseObject(this, pLenient)

fun String.parseJsonArray(): JsonArray =
    sharedGson.parseArray(this)

fun Reader.parseJsonArray(): JsonArray =
    sharedGson.parseArray(this)

fun <T> String.fromJson(pClazz: Class<T>, pLenient: Boolean = false): T =
    sharedGson.fromJson(this, pClazz, pLenient)

fun <T> Reader.fromJson(pClazz: Class<T>, pLenient: Boolean = false): T =
    sharedGson.fromJson(this, pClazz, pLenient)

fun <T> String.fromJson(pType: TypeToken<T>, pLenient: Boolean = false): T? =
    sharedGson.fromJson(this, pType, pLenient)

fun <T> Reader.fromJson(pType: TypeToken<T>, pLenient: Boolean = false): T? =
    sharedGson.fromJson(this, pType, pLenient)