package org.astu.feature.bulletinBoard.views.entities.attachments

import androidx.compose.runtime.mutableStateOf
import org.astu.feature.bulletinBoard.common.utils.calculateVotersPercentage
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.details.QuestionDetails
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.details.SurveyDetails
import org.astu.feature.bulletinBoard.views.components.attachments.common.models.AttachmentContentBase
import org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models.*
import org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.models.*
import org.astu.feature.bulletinBoard.views.components.attachments.voting.surveys.AttachedSurveyContent
import org.astu.feature.bulletinBoard.views.components.attachments.voting.surveys.SurveyContentBase
import org.astu.feature.bulletinBoard.views.entities.users.UserToPresentationMappers.toPresentation
import org.astu.feature.bulletinBoard.views.entities.users.UserToPresentationMappers.toPresentations
import org.astu.feature.bulletinBoard.views.entities.users.UserToViewMappers.toView
import kotlin.jvm.JvmName

object AttachmentToPresentationMappers {
    /**
     * @param TVotedAnswer Используется только если на выходе метода должен быть вопрос с вариантами ответов типа VotedAnswerContentBase
     */
    inline fun <reified TVotedAnswer : VotedAnswerContentBase> mapAttachments(surveys: List<SurveyDetails>?, showVoters: Boolean): List<AttachmentContentBase> {
        return surveys.toPresentations<TVotedAnswer>(showVoters)
    }

    /* *************************************** Survey *************************************** */
    /**
     * @param TVotedAnswer Используется только если на выходе метода должен быть вопрос с вариантами ответов типа VotedAnswerContentBase
     */
    inline fun <reified TVotedAnswer : VotedAnswerContentBase> SurveyDetails.votedSurveyToPresentation(showVoters: Boolean): SurveyContentBase =
        AttachedSurveyContent(
            id = this.id,
            questions = mapVotedQuestions<TVotedAnswer>(this.questions, this.votersAmount),
            voters = this.voters.toPresentations(),
            isVotedByUser = this.isVotedByUser,
            showVoters = showVoters,
            isOpen = this.isOpen,
            showResults = this.showResults,
            isAnonymous = this.isAnonymous,
            autoClosingAt = this.autoClosingAt
        )

    /**
     * @param showVoters показывать список проголосовавших
     * @param showOnlyResults показывать только результаты, если они доступны, без элементов, позволяющих проголосовать
     * @param TVotedAnswer Используется только если на выходе метода должен быть вопрос с вариантами ответов типа VotedAnswerContentBase
     */
    @JvmName("SurveyDetailsToPresentation")
    inline fun <reified TVotedAnswer : VotedAnswerContentBase> SurveyDetails.toPresentation(
        showVoters: Boolean,
        showOnlyResults: Boolean = false
    ): SurveyContentBase =
        AttachedSurveyContent(
            id = this.id,
            questions = mapQuestions<TVotedAnswer>(
                showOnlyResults = showOnlyResults,
                questions = this.questions,
                showResults = this.showResults,
                surveyVotersAmount = this.votersAmount,
                isOpen = this.isOpen,
                isVotedByCurrentUser = this.isVotedByUser
            ),
            voters = this.voters.toPresentations(),
            showVoters = showVoters,
            isVotedByUser = this.isVotedByUser,
            isOpen = this.isOpen,
            showResults = this.showResults,
            isAnonymous = this.isAnonymous,
            autoClosingAt = this.autoClosingAt
        )

    /**
     * @param TVotedAnswer Используется только если на выходе метода должен быть вопрос с вариантами ответов типа VotedAnswerContentBase
     */
    @JvmName("SurveyDetailsCollectionToPresentations")
    inline fun <reified TVotedAnswer : VotedAnswerContentBase> Collection<SurveyDetails>?.toPresentations(
        showVoters: Boolean,
        showOnlyResults: Boolean = false
    ): List<AttachmentContentBase> =
        this?.map { it.toPresentation<TVotedAnswer>(showVoters, showOnlyResults) } ?: emptyList()

    /**
     * @param TVotedAnswer Используется только если на выходе метода должен быть вопрос с вариантами ответов типа VotedAnswerContentBase
     */
    inline fun <reified TVotedAnswer: VotedAnswerContentBase> mapVotedQuestions(
        questions: List<QuestionDetails>,
        surveyVotersAmount: Int,
    ): List<VotedQuestionContent> {
        return questions
            .sortedBy { question -> question.serial }
            .map { mapVotedQuestion<TVotedAnswer>(it, surveyVotersAmount) }
    }

    /**
     * @param TVotedAnswer Используется только если на выходе метода должен быть вопрос с вариантами ответов типа VotedAnswerContentBase
     */
    inline fun <reified TVotedAnswer: VotedAnswerContentBase> mapQuestions(
        questions: List<QuestionDetails>,
        showResults: Boolean,
        isVotedByCurrentUser: Boolean,
        surveyVotersAmount: Int,
        isOpen: Boolean,
        showOnlyResults: Boolean = false,
    ): List<QuestionContentBase> {
        return questions
            .sortedBy { question -> question.serial }
            .map { question ->
                if (isVotedByCurrentUser || !isOpen || showOnlyResults) {
                    mapVotedQuestion<TVotedAnswer>(question, surveyVotersAmount)
                } else if ((isVotedByCurrentUser || isOpen) && !showResults || !isOpen) {
                    mapClosedResultsQuestion(question)
                } else if (question.isMultipleChoiceAllowed) {
                    mapMultipleChoiceQuestion(question)
                } else {
                    mapSingleChoiceQuestion(question)
                }
         }
    }

    inline fun <reified TAnswer: VotedAnswerContentBase> mapVotedQuestion(question: QuestionDetails, surveyVotersAmount: Int): VotedQuestionContent {
        val answers = question.answers
            .sortedBy { answer -> answer.serial }
            .map { answer ->
                when (TAnswer::class) {
                    VotedAnswerContentSummary::class -> VotedAnswerContentSummary(
                        answer.id,
                        answer.content,
                        calculateVotersPercentage(answer.votersAmount, surveyVotersAmount)
                    )
                    VotedAnswerContentDetails::class -> VotedAnswerContentDetails(
                        answer.id,
                        answer.content,
                        calculateVotersPercentage(answer.votersAmount, surveyVotersAmount),
                        voters = answer.voters?.map { it.toPresentation().toView() }
                    )

                    else -> { throw IllegalArgumentException("Неизвестный тип варианта ответа: ${TAnswer::class}") }
                }
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