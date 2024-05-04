package org.astu.feature.bulletinBoard.common.utils

import kotlin.math.roundToInt

/**
 * Подсчет количества проголосовавших за вариант ответа пользователей
 * @param answerVotersCount количество пользователей, проголосовавших за вариант ответа
 * @param surveyVotersCount количество пользователей, проголосовавших в опросе
 */
fun calculateVotersPercentage(answerVotersCount: Int, surveyVotersCount: Int): Int {
    if (surveyVotersCount == 0)
        return 0

    return (answerVotersCount.toDouble() / surveyVotersCount).roundToInt()
}