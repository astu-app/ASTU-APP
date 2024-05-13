package org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses

enum class GetDelayedHiddenAnnouncementListErrors {
    // 403
    /** Пользователь не имеет права просматривать списки объявлений, ожидающих отложенное сокрытие */
    GetDelayedHiddenAnnouncementListAccessForbidden;
}