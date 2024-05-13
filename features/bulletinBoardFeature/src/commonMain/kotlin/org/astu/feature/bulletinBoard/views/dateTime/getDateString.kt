package org.astu.feature.bulletinBoard.views.dateTime

/**
 * Получение даты в указанном формате из количества миллисекунд от начала эпохи
 */
inline fun getDateString(millis: Long, pattern: String = "dd/MM/yyyy"): String {
    val dateTime = getDateTimeFromEpochMillis(millis)
    return getDateTimeString(dateTime, pattern)
}
