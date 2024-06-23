package org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys

import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.dtos.creation.*
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.creation.CreateAnswer
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.creation.CreateQuestion
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.creation.CreateSurvey
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.voting.VoteInQuestion
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.voting.VoteInSurvey
import kotlin.jvm.JvmName

object SurveyToDtoMappers {
    @JvmName("CreateSurveyToDto")
    fun CreateSurvey.toDto(): CreateSurveyDto =
        CreateSurveyDto(
            questions = this.questions.toDtos(),
            isAnonymous = this.isAnonymous,
            resultsOpenBeforeClosing = this.resultsOpenBeforeClosing,
            voteUntil = this.voteUntil,
            rootUserGroupId = this.rootUserGroupId.toString()
        )

    @JvmName("CreateQuestionCollectionToDtos")
    fun Collection<CreateQuestion>.toDtos(): List<CreateQuestionDto> =
        this.map { it.toDto() }

    @JvmName("CreateQuestionToDto")
    fun CreateQuestion.toDto(): CreateQuestionDto =
        CreateQuestionDto(this.serial, this.content, this.isMultipleChoiceAllowed, this.answers.toDtos())

    @JvmName("CreateAnswerCollectionToDtos")
    fun Collection<CreateAnswer>.toDtos(): List<CreateAnswerDto> =
        this.map { it.toDto() }

    @JvmName("CreateAnswerToDto")
    fun CreateAnswer.toDto(): CreateAnswerDto =
        CreateAnswerDto(this.serial, this.content)

    @JvmName("VoteInSurveyToDto")
    fun VoteInSurvey.toDto(): VoteInSurveyDto =
        VoteInSurveyDto(this.surveyId.toString(), this.questionVotes.toDtos())

    @JvmName("VoteInQuestionCollectionToDtos")
    fun Collection<VoteInQuestion>.toDtos(): List<VoteInQuestionDto> =
        map { it.toDto() }

    @JvmName("VoteInQuestionToDto")
    fun VoteInQuestion.toDto(): VoteInQuestionDto =
        VoteInQuestionDto(this.questionId.toString(), this.answerIds.map { it.toString() })
}