package org.astu.feature.bulletinBoard.common.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun localDateTimeFromComponents(
    dateMillis: Long,
    timeHours: Int,
    timeMinutes: Int
): LocalDateTime {
    val instant = Instant.fromEpochMilliseconds(dateMillis)
    val date = instant.toLocalDateTime(TimeZone.currentSystemDefault())

    return LocalDateTime(
        year = date.year,
        monthNumber = date.monthNumber,
        dayOfMonth = date.dayOfMonth,
        hour = timeHours,
        minute = timeMinutes,
    )
}