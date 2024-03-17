package org.astu.app.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import org.astu.app.components.bulletinBoard.announcements.AnnouncementSummary
import org.astu.app.components.bulletinBoard.announcements.models.AnnouncementSummaryContent
import org.astu.app.components.bulletinBoard.attachments.files.FileSummary
import org.astu.app.components.bulletinBoard.attachments.surveys.answers.models.MultipleChoiceAnswerContent
import org.astu.app.components.bulletinBoard.attachments.surveys.answers.models.SingleChoiceAnswerContent
import org.astu.app.components.bulletinBoard.attachments.surveys.answers.models.VotedAnswerContent
import org.astu.app.components.bulletinBoard.attachments.surveys.common.models.SurveyContent
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.MultipleChoiceQuestionContent
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.SingleChoiceQuestionContent
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.VotedQuestionContent

class BulletInBoardScreen : Screen {
    private val announcements: MutableList<AnnouncementSummaryContent> = mutableListOf()

    init {
        getAnnouncements()
    }

    @Composable
    override fun Content() {
        val scrollState = rememberScrollState()
        LaunchedEffect(Unit) { scrollState.animateScrollTo(100) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            announcements.forEach {
                AnnouncementSummary(it)
            }
        }
    }



    private fun getAnnouncements() {
        announcements.add(constructAnnouncementWithFiles())
        announcements.add(constructAnnouncementWithMultipleQuestionsSurvey(10))
        announcements.add(constructAnnouncementWithFilesAndSurvey())
        announcements.add(constructAnnouncementWithVotedSurvey())
        announcements.add(constructAnnouncementWithSingleChoiceSurvey())
        announcements.add(constructAnnouncementWithMultipleChoiceSurvey())
        announcements.add(constructAnnouncementWithOnlyText())
    }

    private fun constructAnnouncementWithFiles(): AnnouncementSummaryContent {
        return AnnouncementSummaryContent(
            author = "Белов Сергей Валерьевич",
            publicationTime = "15 фев 15:50",
            text = "Текст объявления с документами",
            viewed = 145,
            audienceSize = 300,
            attachments = listOf(
                FileSummary("Документ.docx", "20 мб"),
                FileSummary("Презентация.pptx", "20 мб"),
                FileSummary("Таблица.xlsx", "20 мб"),
            )
        )
    }

    private fun constructAnnouncementWithSingleChoiceSurvey(): AnnouncementSummaryContent {
        val answers = listOf(
            SingleChoiceAnswerContent("Ответ 1 на вопрос с единственным выбором"),
            SingleChoiceAnswerContent("Ответ 2 на вопрос с единственным выбором"),
            SingleChoiceAnswerContent("Ответ 3 на вопрос с единственным выбором"),
        )
        val questions = listOf(
            SingleChoiceQuestionContent("Вопрос с единственным выбором", answers)
        )
        val attachments = listOf(
            SurveyContent(questions)
        )

        return AnnouncementSummaryContent(
            author = "Белов Сергей Валерьевич",
            publicationTime = "15 фев 15:50",
            text = "Текст объявления с опросом с единственным выбором",
            viewed = 145,
            audienceSize = 300,
            attachments = attachments
        )
    }

    private fun constructAnnouncementWithMultipleChoiceSurvey(): AnnouncementSummaryContent {
        val answers = listOf(
            MultipleChoiceAnswerContent("Ответ 1 на вопрос с множественным выбором"),
            MultipleChoiceAnswerContent("Ответ 2 на вопрос с множественным выбором"),
            MultipleChoiceAnswerContent("Ответ 3 на вопрос с множественным выбором"),
            MultipleChoiceAnswerContent("Ответ 4 на вопрос с множественным выбором"),
        )
        val questions = listOf(
            MultipleChoiceQuestionContent("Вопрос с множественным выбором", answers)
        )
        val attachments = listOf(
            SurveyContent(questions)
        )

        return AnnouncementSummaryContent(
            author = "Белов Сергей Валерьевич",
            publicationTime = "15 фев 15:50",
            text = "Текст объявления с опросом с множественным выбором",
            viewed = 145,
            audienceSize = 300,
            attachments = attachments
        )
    }

    private fun constructAnnouncementWithVotedSurvey(): AnnouncementSummaryContent {
        val answers = listOf(
            VotedAnswerContent("Ответ 1 на вопрос с множественным выбором", 10),
            VotedAnswerContent("Ответ 2 на вопрос с множественным выбором",30),
            VotedAnswerContent("Ответ 3 на вопрос с множественным выбором", 15),
            VotedAnswerContent("Ответ 4 на вопрос с множественным выбором", 45),
        )
        val questions = listOf(
            VotedQuestionContent("Вопрос с множественным выбором", answers)
        )
        val attachments = listOf(
            SurveyContent(questions)
        )

        return AnnouncementSummaryContent(
            author = "Белов Сергей Валерьевич",
            publicationTime = "15 фев 15:50",
            text = "Текст объявления с опросом с множественным выбором",
            viewed = 145,
            audienceSize = 300,
            attachments = attachments
        )
    }

    private fun constructAnnouncementWithMultipleQuestionsSurvey(amount: Int): AnnouncementSummaryContent {
        val questions = mutableListOf<MultipleChoiceQuestionContent>()
        for (i in 1 .. amount) {
            val answers = listOf(
                MultipleChoiceAnswerContent("Ответ 1 на вопрос $i"),
                MultipleChoiceAnswerContent("Ответ 2 на вопрос $i"),
                MultipleChoiceAnswerContent("Ответ 3 на вопрос $i"),
            )
            val question = MultipleChoiceQuestionContent("Вопрос $i с множественным выбором", answers)
            questions.add(question)
        }

        val attachments = listOf(
            SurveyContent(questions)
        )

        return AnnouncementSummaryContent(
            author = "Белов Сергей Валерьевич",
            publicationTime = "15 фев 15:50",
            text = "Текст объявления с опросом с множеством вопросов",
            viewed = 145,
            audienceSize = 300,
            attachments = attachments
        )
    }

    private fun constructAnnouncementWithOnlyText(): AnnouncementSummaryContent {
        return AnnouncementSummaryContent(
            author = "Белов Сергей Валерьевич",
            publicationTime = "15 фев 15:50",
            text = "Текст простого объявления",
            viewed = 145,
            audienceSize = 300,
            attachments = null
        )
    }

    private fun constructAnnouncementWithFilesAndSurvey(): AnnouncementSummaryContent {
        val answers = listOf(
            SingleChoiceAnswerContent("Ответ 1"),
            SingleChoiceAnswerContent("Ответ 2"),
            SingleChoiceAnswerContent("Ответ 3"),
        )
        val questions = listOf(
            SingleChoiceQuestionContent("Вопрос 1", answers)
        )
        val attachments = listOf(
            SurveyContent(questions),
            FileSummary("Документ.docx", "20 мб"),
        )

        return AnnouncementSummaryContent(
            author = "Белов Сергей Валерьевич",
            publicationTime = "15 фев 15:50",
            text = "Текст объявления с опросом с множественным выбором",
            viewed = 145,
            audienceSize = 300,
            attachments = attachments
        )
    }
}