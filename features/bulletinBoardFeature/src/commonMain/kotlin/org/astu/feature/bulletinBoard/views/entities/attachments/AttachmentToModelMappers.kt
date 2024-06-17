package org.astu.feature.bulletinBoard.views.entities.attachments

import org.astu.feature.bulletinBoard.models.entities.attachments.survey.creation.CreateAnswer
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.creation.CreateQuestion
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.creation.CreateSurvey
import org.astu.feature.bulletinBoard.views.entities.attachments.creation.NewAnswer
import org.astu.feature.bulletinBoard.views.entities.attachments.creation.NewQuestion
import org.astu.feature.bulletinBoard.views.entities.attachments.creation.NewSurvey
import kotlin.jvm.JvmName

object AttachmentToModelMappers {
    @JvmName("NewSurveyToModel")
    fun NewSurvey.toModel(): CreateSurvey =
        CreateSurvey(
            questions = this.questions.toModels(),
            isAnonymous = this.isAnonymous.value,
            resultsOpenBeforeClosing = this.resultsOpenBeforeClosing.value,
            voteUntil = this.autoClosingMoment
        )

    @JvmName("NewQuestionCollectionToModels")
    fun Collection<NewQuestion>.toModels(): List<CreateQuestion> =
        this.mapIndexed { index, question -> question.toModel(index) }

    @JvmName("NewQuestionToModel")
    fun NewQuestion.toModel(serial: Int): CreateQuestion =
        CreateQuestion(
            serial = serial,
            content = this.content.value,
            isMultipleChoiceAllowed = this.isMultipleChoiceAllowed.value,
            answers = this.answers.values.toModels()
        )

    @JvmName("NewAnswerCollectionToModels")
    fun Collection<NewAnswer>.toModels(): List<CreateAnswer> =
        this.mapIndexed { index, answer -> answer.toModel(index) }

    @JvmName("NewAnswerToModel")
    fun NewAnswer.toModel(serial: Int): CreateAnswer =
        CreateAnswer(serial, this.content.value)
}