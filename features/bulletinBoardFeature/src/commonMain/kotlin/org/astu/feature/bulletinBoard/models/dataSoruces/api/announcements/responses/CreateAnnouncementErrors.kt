@file:Suppress("unused") // используется в соответствующем методе источника данных

package org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses

enum class CreateAnnouncementErrors {
    // 400
    /** Список аудитории null или пустой */
    AudienceNullOrEmpty,
    /** Текстовое содержимое объявления является null, пустым или состоит только из пробельных символов */
    ContentNullOrEmpty,
    // 403
    /** Пользователь не имеет права создать объявление */
    AnnouncementCreationForbidden,
    // 404
    /** В качестве одного или нескольких из id вложений прикреплен несуществующий в базе id */
    AttachmentsDoNotExist,
    /** В качестве одного или нескольких из id пользователей прикреплен несуществующий в базе id */
    PieceOfAudienceDoesNotExist,
    // 409
    /** Момент отложенной публикации уже наступил в прошлом */
    DelayedPublishingMomentIsInPast,
    /** Момент автоматического сокрытия уже наступил в прошлом */
    DelayedHidingMomentIsInPast,
    /** Момент отложенной публикации наступит после момента отложенного сокрытия */
    DelayedPublishingMomentAfterDelayedHidingMoment;
}