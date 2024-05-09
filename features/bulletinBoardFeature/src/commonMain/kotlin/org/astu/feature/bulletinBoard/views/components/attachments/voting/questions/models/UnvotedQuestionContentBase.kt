package org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.models

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models.UnvotedAnswerContentBase

abstract class UnvotedQuestionContentBase(
    id: Uuid,
    text: String,
    answers: List<UnvotedAnswerContentBase>
) : QuestionContentBase(id, text, answers)