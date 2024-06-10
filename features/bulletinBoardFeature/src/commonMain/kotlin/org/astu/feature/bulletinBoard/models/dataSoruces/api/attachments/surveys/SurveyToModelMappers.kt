package org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys

import com.benasher44.uuid.uuidFrom
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.dtos.details.QuestionAnswerDetailsDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.dtos.details.QuestionDetailsDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.dtos.details.SurveyDetailsDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.users.UserMappers.toModels
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.details.AnswerDetails
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.details.QuestionDetails
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.details.SurveyDetails
import kotlin.jvm.JvmName

object SurveyToModelMappers {
    @JvmName("SurveyDetailsDtoToModel")
    fun SurveyDetailsDto.toModel(): SurveyDetails =
        SurveyDetails(
            id = uuidFrom(this.id),
            isVotedByUser = this.isVotedByRequester,
            isOpen = this.isOpen,
            isAnonymous = this.isAnonymous,
            resultsOpenBeforeClosing = this.resultsOpenBeforeClosing,
            votersAmount = this.voters.size,
            autoClosingAt = this.autoClosingAt,
            voteFinishedAt = this.voteFinishedAt,
            voters = this.voters.toModels(),
            questions = this.questions.toModels(this.isAnonymous, !this.isVotedByRequester)
        )

    @JvmName("SurveyDetailsDtoCollectionToModels")
    fun Collection<SurveyDetailsDto>?.toModels(): List<SurveyDetails> =
        this?.map { it.toModel() } ?: emptyList()

    @JvmName("QuestionDetailsDtoToModel")
    fun QuestionDetailsDto.toModel(isSurveyAnonymous: Boolean, canVote: Boolean): QuestionDetails =
        QuestionDetails(
            uuidFrom(this.id),
            this.serial,
            this.content,
            this.isMultipleChoiceAllowed,
            this.answers.toModels(isSurveyAnonymous, canVote)
        )

    @JvmName("QuestionDetailsDtoCollectionToModels")
    fun Collection<QuestionDetailsDto>.toModels(isSurveyAnonymous: Boolean, canVote: Boolean): List<QuestionDetails> =
        this.sortedBy { it.serial }.map { it.toModel(isSurveyAnonymous, canVote) }

    @JvmName("QuestionAnswerDetailsDtoToModel")
    fun QuestionAnswerDetailsDto.toModel(isSurveyAnonymous: Boolean, canVote: Boolean): AnswerDetails =
        AnswerDetails(
            id = uuidFrom(this.id),
            serial = this.serial,
            content = this.content,
            voters = if (!isSurveyAnonymous) this.voters.toModels() else null,
            votersAmount = this.votersAmount,
            canVote = canVote
        )

    @JvmName("QuestionAnswerDetailsDtoCollectionToModels")
    fun Collection<QuestionAnswerDetailsDto>.toModels(isSurveyAnonymous: Boolean, canVote: Boolean): List<AnswerDetails> =
        this.sortedBy { it.serial }.map { it.toModel(isSurveyAnonymous, canVote) }
}