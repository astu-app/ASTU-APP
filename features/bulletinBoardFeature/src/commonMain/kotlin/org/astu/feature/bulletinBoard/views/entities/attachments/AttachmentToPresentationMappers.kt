package org.astu.feature.bulletinBoard.views.entities.attachments

import androidx.compose.runtime.mutableStateOf
import org.astu.feature.bulletinBoard.common.utils.calculateVotersPercentage
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.details.QuestionDetails
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.details.SurveyDetails
import org.astu.feature.bulletinBoard.views.components.attachments.common.models.AttachmentContentBase
import org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models.MultipleChoiceAnswerContent
import org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models.SingleChoiceAnswerContent
import org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models.VotedAnswerContentSummary
import org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.models.MultipleChoiceQuestionContent
import org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.models.QuestionContentBase
import org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.models.SingleChoiceQuestionContent
import org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.models.VotedQuestionContent
import org.astu.feature.bulletinBoard.views.components.attachments.voting.surveys.AttachedSurveyContent
import org.astu.feature.bulletinBoard.views.components.attachments.voting.surveys.SurveyContentBase
import kotlin.jvm.JvmName

object AttachmentToPresentationMappers {
    fun mapAttachments(surveys: List<SurveyDetails>?): List<AttachmentContentBase> {
        return surveys.toPresentations()
    }

    /* *************************************** Survey *************************************** */
    fun SurveyDetails.votedSurveyToPresentation(): SurveyContentBase =
        AttachedSurveyContent(this.id, mapVotedQuestions(this.questions, this.votersAmount), this.isVotedByUser, this.isOpen)

    @JvmName("SurveyDetailsToPresentation")
    fun SurveyDetails.toPresentation(): SurveyContentBase =
        AttachedSurveyContent(this.id, mapQuestions(this.questions, this.votersAmount, this.voteFinishedAt != null, this.isOpen), this.isVotedByUser, this.isOpen)

    @JvmName("SurveyDetailsCollectionToPresentations")
    fun Collection<SurveyDetails>?.toPresentations(): List<AttachmentContentBase> =
        this?.map { it.toPresentation() } ?: emptyList()

    fun mapVotedQuestions(
        questions: List<QuestionDetails>,
        surveyVotersAmount: Int,
    ): List<VotedQuestionContent> {
        return questions
            .sortedBy { question -> question.serial }
            .map { mapVotedQuestion(it, surveyVotersAmount) }
    }

    fun mapQuestions(
        questions: List<QuestionDetails>,
        surveyVotersAmount: Int,
        voteFinished: Boolean,
        isOpen: Boolean
    ): List<QuestionContentBase> {
        return questions
            .sortedBy { question -> question.serial }
            .map { question ->
                if (voteFinished || !isOpen) {
                    mapVotedQuestion(question, surveyVotersAmount)
                } else if (question.isMultipleChoiceAllowed) {
                    mapMultipleChoiceQuestion(question)
                } else {
                    mapSingleChoiceQuestion(question)
                }
         }
    }

    fun mapVotedQuestion(question: QuestionDetails, surveyVotersAmount: Int): VotedQuestionContent {
        val answers = question.answers
            .sortedBy { answer -> answer.serial }
            .map { answer ->
                VotedAnswerContentSummary(
                    answer.id,
                    answer.content,
                    calculateVotersPercentage(answer.votersAmount, surveyVotersAmount)
                )
            }
        return VotedQuestionContent(question.id, question.content, answers)
    }

    fun mapMultipleChoiceQuestion(question: QuestionDetails): MultipleChoiceQuestionContent {
        val answers = question.answers
            .sortedBy {answer -> answer.serial }
            .map { answer ->
                MultipleChoiceAnswerContent(
                    answer.id,
                    answer.content,
                    mutableStateOf(answer.canVote),
                    mutableStateOf(false)
                )
            }
        return MultipleChoiceQuestionContent(question.id, question.content, answers)
    }

    fun mapSingleChoiceQuestion(question: QuestionDetails): SingleChoiceQuestionContent {
        val answers = question.answers
            .sortedBy {answer -> answer.serial }
            .map { answer -> SingleChoiceAnswerContent(answer.id, answer.content, mutableStateOf(answer.canVote)) }
        return SingleChoiceQuestionContent(question.id, question.content, answers)
    }
}