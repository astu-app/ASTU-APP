package org.astu.feature.bulletinBoard.models.entities.announcements

import com.benasher44.uuid.Uuid
import kotlinx.datetime.LocalDateTime
import org.astu.feature.bulletinBoard.models.entities.attachments.file.creation.CreateFile
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.creation.CreateSurvey
import org.astu.feature.bulletinBoard.models.entities.common.UpdateIdentifierList

/**
 * Объект содержит контент для редактирования объявления
 * @param id Идентификато редактируемого объявления
 * @param content Текстовое содержимое объявления. Null, если значение свойства не изменилось
 * @param categories Объект для обновления списка прикрепленных идентификаторов. Null, если список идентификаторов не требуется изменять
 * @param users Объект для обновления списка прикрепленных идентификаторов. Null, если список идентификаторов не требуется изменять
 * @param attachmentIds Объект для обновления списка прикрепленных идентификаторов. Null, если список идентификаторов не требуется изменять
 * @param delayedPublishingAtChanged Было ли изменено значение момента отложенной публикации
 * @param delayedPublishingAt Момент отложенной публикации объявления. Null, если отложенная публикация не предполагается
 * @param delayedHidingAtChanged Было ли изменено значение момента отложенного сокрытия
 * @param delayedHidingAt Момент отложенного сокрытия объявления. Null, если отложенное сокрытие не предполагается
 */
data class EditAnnouncement (
    val id: Uuid,
    val content: String?,
    val categories: UpdateIdentifierList?,
    val users: UpdateIdentifierList?,
    val delayedPublishingAtChanged: Boolean,
    val delayedPublishingAt: LocalDateTime?,
    val delayedHidingAtChanged: Boolean,
    val delayedHidingAt: LocalDateTime?,

    val attachmentIdsToRemove: Set<Uuid>?,
    val newSurvey: CreateSurvey?,
    val newFiles: List<CreateFile>?,
)