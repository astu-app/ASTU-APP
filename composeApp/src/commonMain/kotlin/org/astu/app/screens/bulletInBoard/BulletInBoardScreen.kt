package org.astu.app.screens.bulletInBoard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.benasher44.uuid.uuidFrom
import org.astu.app.components.bulletinBoard.announcements.summary.AnnouncementSummary
import org.astu.app.components.bulletinBoard.announcements.summary.models.AnnouncementSummaryContent
import org.astu.app.components.bulletinBoard.attachments.files.models.FileDownloadState
import org.astu.app.components.bulletinBoard.attachments.files.models.FileSummary
import org.astu.app.components.bulletinBoard.attachments.surveys.answers.models.MultipleChoiceAnswerContent
import org.astu.app.components.bulletinBoard.attachments.surveys.answers.models.SingleChoiceAnswerContent
import org.astu.app.components.bulletinBoard.attachments.surveys.answers.models.VotedAnswerContentSummary
import org.astu.app.components.bulletinBoard.attachments.surveys.common.models.SurveyContent
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.MultipleChoiceQuestionContent
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.SingleChoiceQuestionContent
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.VotedQuestionContent
import org.astu.app.screens.bulletInBoard.announcementAction.CreateAnnouncementScreen
import org.astu.app.theme.CurrentColorScheme

class BulletInBoardScreen : Screen {
    private val announcements: MutableList<AnnouncementSummaryContent> = mutableListOf()

    init {
        getAnnouncements()
    }

    @Composable
    override fun Content() {
        val scrollState = rememberScrollState()
        LaunchedEffect(Unit) { scrollState.animateScrollTo(100) }

        val navigator = LocalNavigator.currentOrThrow
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val createAnnouncementScreen = CreateAnnouncementScreen { navigator.pop() }
                        navigator.push(createAnnouncementScreen)
                    },
                    containerColor = CurrentColorScheme.tertiaryContainer,
                ) {
                    Icon(Icons.Outlined.Edit, null)
                }
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    announcements.forEach { announcement ->
                        AnnouncementSummary(announcement)
                    }
                }
            }
        )
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
            id = uuidFrom("30220a72-1be1-48e4-8321-4e037cea89ab"),
            author = "Белов Сергей Валерьевич",
            publicationTime = "15 фев 15:50",
            text = "Текст объявления с документами",
            viewed = 145,
            audienceSize = 300,
            attachments = listOf(
                FileSummary("Документ.docx", "20 мб", mutableStateOf(FileDownloadState.NOT_DOWNLOADED)),
                FileSummary("Презентация.pptx", "20 мб", mutableStateOf(FileDownloadState.DOWNLOADING)),
                FileSummary("Таблица.xlsx", "20 мб", mutableStateOf(FileDownloadState.DOWNLOADED)),
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
            id = uuidFrom("816a1287-0f24-4757-ad00-6892888a583e"),
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
            id = uuidFrom("fc9b13dc-5e4e-4764-a523-471171e034a7"),
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
            VotedAnswerContentSummary("Ответ 1 на вопрос с множественным выбором", 10),
            VotedAnswerContentSummary("Ответ 2 на вопрос с множественным выбором", 30),
            VotedAnswerContentSummary("Ответ 3 на вопрос с множественным выбором", 15),
            VotedAnswerContentSummary("Ответ 4 на вопрос с множественным выбором", 45),
        )
        val questions = listOf(
            VotedQuestionContent("Вопрос с множественным выбором", answers)
        )
        val attachments = listOf(
            SurveyContent(questions)
        )

        return AnnouncementSummaryContent(
            id = uuidFrom("b7f6ab67-31c0-47f8-b812-f0f8c2b0497b"),
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
        for (i in 1..amount) {
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
            id = uuidFrom("a66fe755-8d3c-4d0d-800e-16751fbddbe4"),
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
            id = uuidFrom("e26286f1-bb10-449f-bc29-67bea23559bf"),
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
            FileSummary("Документ.docx", "20 мб", mutableStateOf(FileDownloadState.DOWNLOADED)),
        )

        return AnnouncementSummaryContent(
            id = uuidFrom("a212918e-ce44-4fb2-bafa-4bc7bb3d9bd8"),
            author = "Белов Сергей Валерьевич",
            publicationTime = "15 фев 15:50",
            text = "Текст объявления с опросом с множественным выбором",
            viewed = 145,
            audienceSize = 300,
            attachments = attachments
        )
    }
}