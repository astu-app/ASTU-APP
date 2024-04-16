package org.astu.app.view_models.bulletInBoard.humanization

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern

/**
 * Преобразование даты и времени в строку в указанном формате
 */
@OptIn(FormatStringsInDatetimeFormats::class)
fun humanizeDateTime(datetime: LocalDateTime?, pattern: String = "dd/MM в HH:mm"): String {
    if (datetime == null) return ""
    return datetime.format(LocalDateTime.Format {
        byUnicodePattern(pattern)
    })
}