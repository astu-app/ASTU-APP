package org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses

/**
 * Набор данных с типом возможной ошибки, возникшей при их получении при их получении
 * @param TContent Данные, находящиеся в ответе, если данными не является кастомный код ответа. Null, если данные не получены
 * @param TError Представляет тип ошибки. Null, если ошибки не возникло
 */
class ContentWithError<TContent, TError>(val content: TContent?, val error: TError?)
        where TError : Enum<TError> {
    // Менять очень аккуратно, так как на этом свойстве завязано множество логики
    val isContentValid: Boolean = content != null && error == null


    init {
        if (content != null && error != null || content == null && error == null)
            throw IllegalArgumentException("Контент и ошибка должны иметь значения (null, not-null) или (null, not-null)")
    }
}