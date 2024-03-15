package org.astu.app.components.bulletinBoard.attachments.surveys.answers.models

class VotedAnswerContent(
    override val text: String,
    val voterPercent: Int
) : AnswerContentBase()