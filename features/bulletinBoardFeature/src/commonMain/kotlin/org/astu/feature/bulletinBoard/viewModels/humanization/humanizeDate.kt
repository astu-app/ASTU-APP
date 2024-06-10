package org.astu.feature.bulletinBoard.viewModels.humanization

import org.astu.feature.bulletinBoard.views.dateTime.getDateTimeFromEpochMillis

/**
 * Получение даты в указанном формате из количества миллисекунд от начала эпохи
 */
inline fun humanizeDate(millis: Long, pattern: String = "dd/MM/yyyy"): String {
    val dateTime = getDateTimeFromEpochMillis(millis)
    return humanizeDateTime(dateTime, pattern)
}