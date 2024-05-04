package org.astu.feature.bulletinBoard.views.dateTime

import kotlinx.datetime.format.FormatStringsInDatetimeFormats

/**
 * Получение даты в указанном формате из количества миллисекунд от начала эпохи
 */
@OptIn(FormatStringsInDatetimeFormats::class)
inline fun getDateString(millis: Long, pattern: String = "dd/MM/yyyy"): String {
    val dateTime = getDateTimeFromEpochMillis(millis)
    return getDateTimeString(dateTime, pattern)
}
