package com.mealam.showdown.utils.logging

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.logging.ConsoleHandler
import java.util.logging.LogRecord
import java.util.logging.Logger
import java.util.logging.SimpleFormatter

const val RESET = "\u001B[0m"

fun Logger.configure(pColorProvider: ILogColorProvider) {
    val handler = ConsoleHandler()
    handler.formatter = object : SimpleFormatter() {
        override fun format(record: LogRecord): String {
            val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            val color = pColorProvider.getColor(record.level)
            val ansiColor = rgbToAnsi(color)
            val message = buildString {
                append("$ansiColor[$timestamp] [${record.level}]: ${record.message}$RESET")
                record.thrown?.let { append(getExceptionDetails(it)) }
            }
            return "$message\n"
        }

        private fun getExceptionDetails(throwable: Throwable): String =
            buildString {
                append("\nException: ${throwable.message}")
                throwable.stackTrace.forEach { element ->
                    val className = element.className.substringAfterLast('.')
                    val packageName = element.className.substringBeforeLast('.')
                    append("\n\tat $packageName.$className.${element.methodName}(Line: ${element.lineNumber})")
                }
            }
    }
    this.useParentHandlers = false
    this.addHandler(handler)
}

private fun rgbToAnsi(pRgb: Int): String {
    val red = (pRgb shr 16) and 0xFF
    val green = (pRgb shr 8) and 0xFF
    val blue = pRgb and 0xFF
    return "\u001B[38;2;${red};${green};${blue}m"
}