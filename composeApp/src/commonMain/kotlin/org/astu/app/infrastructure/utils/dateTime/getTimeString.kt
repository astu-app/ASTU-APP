package org.astu.app.infrastructure.utils.dateTime

/**
 * Получение времени в формате hour:time. Час представлен одной цифрой, если его значение меньше 10
 */
inline fun getTimeString(hours: Int, minutes: Int): String {
    val hoursString = if (hours < 10) "0$hours" else hours.toString()
    val minutesString = if (minutes < 10) "0$minutes" else minutes.toString()
    return "$hoursString:$minutesString"
}