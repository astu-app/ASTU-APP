package org.astu.feature.bulletinBoard.views.components.attachments.voting.surveys

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.voting.VoteInQuestion
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.voting.VoteInSurvey
import org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models.UnvotedAnswerContentBase
import org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.models.MultipleChoiceQuestionContent
import org.astu.feature.bulletinBoard.views.components.attachments.voting.surveys.models.SurveyVotes
import kotlin.jvm.JvmName

object SurveyMappers {
    @JvmName("SurveyVotesToModel")
    fun SurveyVotes.toModel(): VoteInSurvey =
        VoteInSurvey(this.surveyId, this.questionVotes.toModels())

    @JvmName("QuestionVotesCollectionToModels")
    fun Collection<QuestionVotes>.toModels(): List<VoteInQuestion> =
        this.map { it.toModel() }

    @JvmName("QuestionVotesCollectionToModels")
    fun QuestionVotes.toModel(): VoteInQuestion =
        VoteInQuestion(this.questionId, this.answersVotes)

    @JvmName("AttachedSurveyContentGetVotes")
    fun AttachedSurveyContent.getVotes(): SurveyVotes {
        var questionVotes = mutableStateListOf<QuestionVotes>()
        this.questions.forEach {
            val answerVotes = it.answers
                .filter { (it as? UnvotedAnswerContentBase)?.isSelected() ?: false }
                .map { it.id }
                .toMutableStateList()
            questionVotes.add(QuestionVotes(it.id, it is MultipleChoiceQuestionContent, answerVotes))
        }

        return SurveyVotes(this.id, questionVotes)
    }
}