package org.astu.feature.bulletinBoard.views.entities.attachments

import androidx.compose.runtime.mutableStateOf
import org.astu.feature.bulletinBoard.common.utils.calculateVotersPercentage
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.details.QuestionDetails
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.details.SurveyDetails
import org.astu.feature.bulletinBoard.views.components.attachments.common.models.AttachmentContentBase
import org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models.ClosedResultsAnswerContent
import org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models.MultipleChoiceAnswerContent
import org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models.SingleChoiceAnswerContent
import org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models.VotedAnswerContentSummary
import org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.models.*
import org.astu.feature.bulletinBoard.views.components.attachments.voting.surveys.AttachedSurveyContent
import org.astu.feature.bulletinBoard.views.components.attachments.voting.surveys.SurveyContentBase
import org.astu.feature.bulletinBoard.views.entities.users.UserToPresentationMappers.toPresentations
import kotlin.jvm.JvmName

object AttachmentToPresentationMappers {
    fun mapAttachments(surveys: List<SurveyDetails>?, showVoters: Boolean): List<AttachmentContentBase> {
        return surveys.toPresentations(showVoters)
    }

    /* *************************************** Survey *************************************** */
    fun SurveyDetails.votedSurveyToPresentation(showVoters: Boolean): SurveyContentBase =
        AttachedSurveyContent(
            id = this.id,
            questions = mapVotedQuestions(this.questions, this.votersAmount),
            voters = this.voters.toPresentations(),
            isVotedByUser = this.isVotedByUser,
            showVoters = showVoters,
            isOpen = this.isOpen,
            showResults = this.showResults,
            isAnonymous = this.isAnonymous,
            autoClosingAt = this.autoClosingAt
        )

    @JvmName("SurveyDetailsToPresentation")
    fun SurveyDetails.toPresentation(showVoters: Boolean): SurveyContentBase =
        AttachedSurveyContent(
            id = this.id,
            questions = mapQuestions(
                questions = this.questions,
                showResults = this.showResults,
                surveyVotersAmount = this.votersAmount,
                voteFinished = this.voteFinishedAt != null,
                isOpen = this.isOpen
            ),
            voters = this.voters.toPresentations(),
            showVoters = showVoters,
            isVotedByUser = this.isVotedByUser,
            isOpen = this.isOpen,
            showResults = this.showResults,
            isAnonymous = this.isAnonymous,
            autoClosingAt = this.autoClosingAt
        )

    @JvmName("SurveyDetailsCollectionToPresentations")
    fun Collection<SurveyDetails>?.toPresentations(showVoters: Boolean): List<AttachmentContentBase> =
        this?.map { it.toPresentation(showVoters) } ?: emptyList()

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
        showResults: Boolean,
        surveyVotersAmount: Int,
        voteFinished: Boolean,
        isOpen: Boolean
    ): List<QuestionContentBase> {
        return questions
            .sortedBy { question -> question.serial }
            .map { question ->
                if (!showResults) {
                    mapClosedResultsQuestion(question)
                } else if (voteFinished || !isOpen) {
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

    fun mapClosedResultsQuestion(question: QuestionDetails): ClosedResultsQuestionContent {
        val answers = question.answers
            .sortedBy { answer -> answer.serial }
            .map { answer ->
                ClosedResultsAnswerContent(
                    answer.id,
                    answer.content,
                )
            }
        return ClosedResultsQuestionContent(question.id, question.content, answers)
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