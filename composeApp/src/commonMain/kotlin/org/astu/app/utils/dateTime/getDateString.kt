package org.astu.app.utils.dateTime

import kotlinx.datetime.*
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern

/**
 * Получение даты в указанном формате из количества миллисекунд от начала эпохи
 */
@OptIn(FormatStringsInDatetimeFormats::class)
inline fun getDateString(millis: Long, pattern: String = "dd/MM/yyyy"): String {
    val instant = Instant.fromEpochMilliseconds(millis)
    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

    val dateTimeFormat = LocalDateTime.Format { byUnicodePattern(pattern) }
    return dateTime.format(dateTimeFormat)
}