package org.astu.feature.bulletinBoard.views.dateTime

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Получение даты из миллисекунд, прошедших с начала эпохи
 */
fun getDateTimeFromEpochMillis(millis: Long): LocalDateTime {
    val instant = Instant.fromEpochMilliseconds(millis)
    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return dateTime
}