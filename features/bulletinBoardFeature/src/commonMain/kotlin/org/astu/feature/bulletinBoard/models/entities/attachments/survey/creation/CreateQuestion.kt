package org.astu.feature.bulletinBoard.models.entities.attachments.survey.creation

data class CreateQuestion (
    val serial: Int,
    val content: String,
    val isMultipleChoiceAllowed: Boolean,
    val answers: List<CreateAnswer>
)