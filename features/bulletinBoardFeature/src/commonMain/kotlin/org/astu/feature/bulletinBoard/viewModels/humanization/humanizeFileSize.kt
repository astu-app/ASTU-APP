package org.astu.feature.bulletinBoard.viewModels.humanization

import kotlin.math.abs
import kotlin.math.sign

fun humanizeFileSize(bytes: Long): String {
    // https://stackoverflow.com/questions/3758606/how-can-i-convert-byte-size-into-a-human-readable-format-in-java/26502430#26502430
    val absB = if (bytes == Long.MIN_VALUE) Long.MAX_VALUE else abs(bytes.toDouble()).toLong()
    if (absB < 1024) {
        return "$bytes Б"
    }
    var value = absB

    val suffixes = "КМГТПЭ" // кило-, мега-, гига-, тера-, пета-, экзо-[байты]
    var suffixesPosition = 0;
    var i = 40
    while (i >= 0 && absB >= 0xfffccccccccccccL shr i) {
        value = value shr 10
        suffixesPosition++
        i -= 10
    }
    value *= bytes.sign
    return "${formatDoubleToFirstDecimalDigit(value / 1024.0)} ${suffixes[suffixesPosition]}Б"
}

private fun formatDoubleToFirstDecimalDigit(num: Double): String {
    val tenfold = num * 10
    val tenfoldInt = tenfold.toInt()
    val rounded = tenfoldInt / 10.0
    return rounded.toString()
}