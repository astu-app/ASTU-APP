package org.astu.feature.bulletinBoard.views.dateTime

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern

/**
 * Функция преобразует объект типа LocalDateTime в отформатированную строку
 */
@OptIn(FormatStringsInDatetimeFormats::class)
inline fun getDateTimeString(dateTime: LocalDateTime, pattern: String = "dd/MM HH:mm"): String {
    val dateTimeFormat = LocalDateTime.Format { byUnicodePattern(pattern) }
    return dateTime.format(dateTimeFormat)
}