package org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses

enum class EditAnnouncementErrors {
    // 400
    /** Текстовое содержимое объявления пустое или состоит только из пробельных символов */
    ContentEmpty,
    /** Аудитория пользователей пуста */
    AudienceEmpty,

    // 403
    /** Пользователь не имеет права изменить объявление */
    AnnouncementEditingForbidden,

    // 404
    /** В качестве id объявления прикреплен несуществующий в базе id */
    AnnouncementDoesNotExist,
    /** В качестве одного или нескольких из id вложений прикреплен несуществующий в базе id */
    AttachmentsDoNotExist,
    /** В качестве одного или нескольких из id пользователей прикреплен несуществующий в базе id */
    PieceOfAudienceDoesNotExist,

    // 409
    /** Момент отложенной публикации уже наступил в прошлом */
    DelayedPublishingMomentIsInPast,
    /** Момент отложенного сокрытия уже наступил в прошлом */
    DelayedHidingMomentIsInPast,
    /** Попытка задать срок автоматического сокрытия объявлению, которое уже скрыто */
    AutoHidingAnAlreadyHiddenAnnouncement,
    /** Попытка задать срок автоматической публикации объявлению, которое уже было опубликовано и в настоящий момент не является скрытым */
    AutoPublishingPublishedAndNonHiddenAnnouncement,
    /** Открепление опросов запрещено */
    CannotDetachSurvey;
}