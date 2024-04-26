@file:Suppress("unused") // используется в соответствующем методе источника данных

package org.astu.app.dataSources.bulletInBoard.announcements.common.responses

enum class GetAnnouncementDetailsErrors {
    // 403
    /** Пользователь не имеет права просмотреть подробности объявления */
    DetailsAccessForbidden,
    // 404
    /** Объявление не существует */
    AnnouncementDoesNotExist,
}