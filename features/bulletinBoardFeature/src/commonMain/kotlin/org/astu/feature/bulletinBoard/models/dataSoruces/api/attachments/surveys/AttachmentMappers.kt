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
            uuidFrom(this.id),
            this.isOpen,
            this.isAnonymous,
            this.votersAmount,
            this.autoClosingAt,
            this.voteFinishedAt,
            this.questions.toModels()
        )

    @JvmName("SurveyDetailsDtoCollectionToModels")
    fun Collection<SurveyDetailsDto>?.toModels(): List<SurveyDetails> =
        this?.map { it.toModel() } ?: emptyList()

    @JvmName("QuestionDetailsDtoToModel")
    fun QuestionDetailsDto.toModel(): QuestionDetails =
        QuestionDetails(
            uuidFrom(this.id),
            this.serial,
            this.content,
            this.isMultipleChoiceAllowed,
            this.answers.toModels()
        )

    @JvmName("QuestionDetailsDtoCollectionToModels")
    fun Collection<QuestionDetailsDto>.toModels(): List<QuestionDetails> =
        this.map { it.toModel() }

    @JvmName("QuestionAnswerDetailsDtoToModel")
    fun QuestionAnswerDetailsDto.toModel(): AnswerDetails =
        AnswerDetails(uuidFrom(this.id), this.serial, this.content, this.votersAmount)

    @JvmName("QuestionAnswerDetailsDtoCollectionToModels")
    fun Collection<QuestionAnswerDetailsDto>.toModels(): List<AnswerDetails> =
        this.map { it.toModel() }
}