package org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys

import com.benasher44.uuid.uuidFrom
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.dtos.QuestionAnswerDetailsDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.dtos.QuestionDetailsDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.dtos.SurveyDetailsDto
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.details.AnswerDetails
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.details.QuestionDetails
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.details.SurveyDetails
import kotlin.jvm.JvmName

object AttachmentMappers {
    @JvmName("SurveyDetailsDtoToModel")
    fun SurveyDetailsDto.toModel(): SurveyDetails =
        SurveyDetails(
            id = uuidFrom(this.id),
            isVotedByUser = this.isVotedByRequester,
            isOpen = this.isOpen,
            isAnonymous = this.isAnonymous,
            resultsOpenBeforeClosing = this.resultsOpenBeforeClosing,
            votersAmount = this.votersAmount,
            autoClosingAt = this.autoClosingAt,
            voteFinishedAt = this.voteFinishedAt,
            questions = this.questions.toModels(!this.isVotedByRequester)
        )

    @JvmName("SurveyDetailsDtoCollectionToModels")
    fun Collection<SurveyDetailsDto>?.toModels(): List<SurveyDetails> =
        this?.map { it.toModel() } ?: emptyList()

    @JvmName("QuestionDetailsDtoToModel")
    fun QuestionDetailsDto.toModel(canVote: Boolean = true): QuestionDetails =
        QuestionDetails(
            uuidFrom(this.id),
            this.serial,
            this.content,
            this.isMultipleChoiceAllowed,
            this.answers.toModels(canVote)
        )

    @JvmName("QuestionDetailsDtoCollectionToModels")
    fun Collection<QuestionDetailsDto>.toModels(canVote: Boolean = true): List<QuestionDetails> =
        this.map { it.toModel(canVote) }

    @JvmName("QuestionAnswerDetailsDtoToModel")
    fun QuestionAnswerDetailsDto.toModel(canVote: Boolean = true): AnswerDetails =
        AnswerDetails(uuidFrom(this.id), this.serial, this.content, this.votersAmount, canVote)

    @JvmName("QuestionAnswerDetailsDtoCollectionToModels")
    fun Collection<QuestionAnswerDetailsDto>.toModels(canVote: Boolean = true): List<AnswerDetails> =
        this.map { it.toModel(canVote) }
}