package org.astu.feature.bulletinBoard.viewModels.humanization

import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.*
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.files.responses.UploadFilesErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.responses.CreateSurveyErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.responses.VoteInSurveyErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.DeleteUserGroupErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.GetUserHierarchyErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.GetUsergroupDetailsErrors

object ErrorCodesHumanization {
    private const val DEFAULT_ERROR_MESSAGE: String = "Неожиданная ошибка. Повторите попытку"

    /* ******************************************** Объявления ******************************************** */

    fun GetAnnouncementDetailsErrors?.humanize(): String =
        when (this) {
            GetAnnouncementDetailsErrors.DetailsAccessForbidden -> "У вас недостаточно прав для просмотра подробностей этого объявления"
            GetAnnouncementDetailsErrors.AnnouncementDoesNotExist -> "Объявление не найдено"
            else -> DEFAULT_ERROR_MESSAGE
        }

    fun CreateAnnouncementErrorsAggregate?.humanize(): String {
        if (this?.createAnnouncementError != null)
            return this.createAnnouncementError.humanize()

        if (this?.createSurveyError != null)
            return this.createSurveyError.humanize()

        if (this?.createFilesError != null)
            return this.createFilesError.humanize()

        return DEFAULT_ERROR_MESSAGE
    }

    private fun CreateAnnouncementErrors?.humanize(): String =
        when (this) {
            CreateAnnouncementErrors.AudienceNullOrEmpty -> "Аудитория объявления не задана"
            CreateAnnouncementErrors.ContentNullOrEmpty -> "Текст объявления не задан"
            CreateAnnouncementErrors.AnnouncementCreationForbidden -> "У вас недостаточно прав для создания объявлений"
            CreateAnnouncementErrors.AnnouncementCategoriesDoNotExist -> "Не удалось прикрепить одно или несколько категорий объявлений"
            CreateAnnouncementErrors.AttachmentsDoNotExist -> "Не удалось прикрепить одно или несколько вложений"
            CreateAnnouncementErrors.PieceOfAudienceDoesNotExist -> "Не удалось прикрепить одного или нескольких из указанных пользователей"
            CreateAnnouncementErrors.DelayedPublishingMomentIsInPast -> "Момент отложенной публикации объявления не может наступить в прошлом"
            CreateAnnouncementErrors.DelayedHidingMomentIsInPast -> "Момент отложенного сокрытия объявления не может наступить в прошлом"
            CreateAnnouncementErrors.DelayedPublishingMomentAfterDelayedHidingMoment -> "Момент отложенной публикации объявления не может наступить после момента отложенного сокрытия"
            else -> DEFAULT_ERROR_MESSAGE
        }

    fun GetAnnouncementEditContentErrors?.humanize(): String =
        when (this) {
            GetAnnouncementEditContentErrors.GetAnnouncementUpdateContentForbidden -> "У вас недостаточно прав для загрузки данных для редактирования объявления"
            GetAnnouncementEditContentErrors.AnnouncementDoesNotExist -> "Объявление не найдено. Повторите попытку позднее"
            else -> DEFAULT_ERROR_MESSAGE
        }

    fun EditAnnouncementErrorsAggregate?.humanize(): String {
        if (this?.editAnnouncementError != null)
            return this.editAnnouncementError.humanize()

        if (this?.createSurveyError != null)
            return this.createSurveyError.humanize()

        if (this?.createFilesError != null)
            return this.createFilesError.humanize()

        return DEFAULT_ERROR_MESSAGE
    }

    fun EditAnnouncementErrors?.humanize(): String =
        when (this) {
            EditAnnouncementErrors.ContentEmpty -> "Текст объявления не должен быть пустым"
            EditAnnouncementErrors.AudienceEmpty -> "Нельзя очистить аудиторию объявления"
            EditAnnouncementErrors.AnnouncementEditingForbidden -> "У вас недостаточно прав для изменения этого объявления"
            EditAnnouncementErrors.AnnouncementDoesNotExist -> "Объявление не найдено. Повторите попытку позднее"
            EditAnnouncementErrors.AnnouncementCategoriesDoesNotExist -> "Категория объявлений не найдена. Повторите попытку позднее"
            EditAnnouncementErrors.AttachmentsDoNotExist -> "Вложение не найдено. Повторите попытку позднее"
            EditAnnouncementErrors.PieceOfAudienceDoesNotExist -> "Пользователь не найден. Повторите попытку позднее"
            EditAnnouncementErrors.DelayedPublishingMomentIsInPast -> "Момент отложенной публикации уже наступил в прошлом"
            EditAnnouncementErrors.DelayedHidingMomentIsInPast -> "Момент отложенного сокрытия уже наступил в прошлом"
            EditAnnouncementErrors.AutoHidingAnAlreadyHiddenAnnouncement -> "Нельзя задать момент отложенного сокрытия объявлению, которое уже было скрыто"
            EditAnnouncementErrors.AutoPublishingPublishedAndNonHiddenAnnouncement -> "Нельзя задать момент автоматической публикации объявлению, которое уже было опубликовано"
            EditAnnouncementErrors.CannotDetachSurvey -> "Нельзя открепить опрос"
            else -> DEFAULT_ERROR_MESSAGE
        }

    fun CreateSurveyErrors?.humanize(): String =
        when (this) {
            CreateSurveyErrors.CreateSurveyForbidden -> "У вас недостаточно прав для создания опроса"
            CreateSurveyErrors.SurveyContainsQuestionSerialsDuplicates -> "Опрос содержит вопросы с одинаковыми порядковыми номерами"
            CreateSurveyErrors.QuestionContainsAnswersSerialsDuplicates -> "Вопрос(ы) содержит варианты ответов с одинаковыми порядковыми номерами"
            else -> DEFAULT_ERROR_MESSAGE
        }

    fun UploadFilesErrors?.humanize(): String =
        DEFAULT_ERROR_MESSAGE

    fun GetPostedAnnouncementListErrors?.humanize(): String =
        when (this) {
            GetPostedAnnouncementListErrors.PostedAnnouncementsListAccessForbidden -> "У вас недостаточно прав для просмотра ленты объявлений"
            else -> DEFAULT_ERROR_MESSAGE
        }

    fun HidePostedAnnouncementErrors?.humanize(): String =
        when (this) {
            HidePostedAnnouncementErrors.AnnouncementHidingForbidden -> "У вас недостаточно прав для сокрытия объявления"
            HidePostedAnnouncementErrors.AnnouncementDoesNotExist -> "Объявление не найдено"
            HidePostedAnnouncementErrors.AnnouncementAlreadyHidden -> "Нельзя скрыть уже скрытое объявление"
            HidePostedAnnouncementErrors.AnnouncementNotYetPublished -> "Нельзя скрыть объявление, которое еще не было опубликовано"
            else -> DEFAULT_ERROR_MESSAGE
        }


    fun GetDelayedHiddenAnnouncementListErrors?.humanize(): String =
        when (this) {
            GetDelayedHiddenAnnouncementListErrors.GetDelayedHiddenAnnouncementListAccessForbidden -> "У вас недостаточно прав для просмотра списка объявлений, ожидающих отложенной публикации"
            else -> DEFAULT_ERROR_MESSAGE
        }

    fun GetDelayedPublishedAnnouncementsErrors?.humanize() =
        when (this) {
            GetDelayedPublishedAnnouncementsErrors.GetDelayedPublishingAnnouncementListResponsesAccessForbidden -> "У вас недостаточно прав для просмотра списка объявлений, ожидающих отложенного сокрытия"
            else -> DEFAULT_ERROR_MESSAGE
        }

    fun DeleteAnnouncementErrors?.humanize() =
        when (this) {
            DeleteAnnouncementErrors.AnnouncementDeletionForbidden -> "У вас недостаточно прав для удаления объявления"
            DeleteAnnouncementErrors.AnnouncementDoesNotExist -> "Объявление не найдено"
            else -> DEFAULT_ERROR_MESSAGE
        }

    fun PublishImmediatelyDelayedAnnouncementErrors?.humanize() =
        when (this) {
            PublishImmediatelyDelayedAnnouncementErrors.ImmediatePublishingForbidden -> "У вас недостаточно прав для немедленной публикации объявления"
            PublishImmediatelyDelayedAnnouncementErrors.AnnouncementDoesNotExist -> "Объявление не найдено"
            else -> DEFAULT_ERROR_MESSAGE
        }

    fun GetHiddenAnnouncementListErrors?.humanize(): String =
        when (this) {
            GetHiddenAnnouncementListErrors.HiddenAnnouncementsListAccessForbidden -> "У вас недостаточно прав для просмотра списка скрытых объявлений"
            else -> DEFAULT_ERROR_MESSAGE
        }

    fun RestoreHiddenAnnouncementErrors?.humanize(): String =
        when (this) {
            RestoreHiddenAnnouncementErrors.RestoreForbidden -> "У вас недостаточно прав для восстановления объявления"
            RestoreHiddenAnnouncementErrors.AnnouncementDoesNotExist -> "Объявление не найдено"
            RestoreHiddenAnnouncementErrors.AnnouncementNotHidden -> "Нельзя восстановить объявление, которое не было скрыто"
            else -> DEFAULT_ERROR_MESSAGE
        }

    /* ******************************************** Опросы ******************************************** */

    fun VoteInSurveyErrors?.humanize(): String =
        when (this) {
            VoteInSurveyErrors.VotingForbidden -> "У вас недостаточно прав для голосования в этом опросе"
            VoteInSurveyErrors.SurveyDoesNotExist -> "Опрос не найден. Повторите попытку позже"
            VoteInSurveyErrors.SurveyClosed -> "Нельзя проголосовать в закрытом опросе"
            VoteInSurveyErrors.SurveyAlreadyVoted -> "Нельзя проголосовать в опросе дважды"
            VoteInSurveyErrors.CannotSelectMultipleAnswersInSingleChoiceQuestion -> "В вопросе с единственным выбором нельзя выбрать несколько вариантов ответов"
            else -> DEFAULT_ERROR_MESSAGE
        }

    /* ******************************************** Группы пользователей ******************************************** */

    fun GetUserHierarchyErrors?.humanize(): String =
        when (this) {
            GetUserHierarchyErrors.GetUsergroupHierarchyForbidden -> "У вас недостаточно прав для просмотра списка групп пользователей"
            else -> DEFAULT_ERROR_MESSAGE
        }

    fun DeleteUserGroupErrors?.humanize(): String =
        when (this) {
            DeleteUserGroupErrors.UsergroupDeletionForbidden -> "У вас отсутствуют права на удаление группы пользователей"
            DeleteUserGroupErrors.UserGroupDoesNotExist -> "Группа пользователей не найдена"
            else -> DEFAULT_ERROR_MESSAGE
        }

    fun GetUsergroupDetailsErrors?.humanize(): String =
        when (this) {
            GetUsergroupDetailsErrors.UetUsergroupDetailsForbidden -> "У вас недостаточно прав для просмотра деталей этой группы пользователей"
            GetUsergroupDetailsErrors.UserGroupDoesNotExist -> "Группа пользователей не найдена"
            else -> DEFAULT_ERROR_MESSAGE
        }
}