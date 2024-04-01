package org.astu.app.screens.bulletInBoard.announcementAction

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.benasher44.uuid.uuid4
import org.astu.app.components.bulletinBoard.announcements.details.AnnouncementDetails
import org.astu.app.components.bulletinBoard.announcements.details.models.AnnouncementDetailsContent
import org.astu.app.components.bulletinBoard.announcements.details.models.audienceGraph.INode
import org.astu.app.components.bulletinBoard.announcements.details.models.audienceGraph.Leaf
import org.astu.app.components.bulletinBoard.announcements.details.models.audienceGraph.Node
import org.astu.app.components.bulletinBoard.attachments.common.models.AttachmentBase
import org.astu.app.components.bulletinBoard.attachments.files.models.FileDownloadState
import org.astu.app.components.bulletinBoard.attachments.files.models.FileSummary
import org.astu.app.components.bulletinBoard.attachments.surveys.answers.models.VotedAnswerContentDetails
import org.astu.app.components.bulletinBoard.attachments.surveys.common.models.SurveyContent
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.VotedQuestionContent
import org.astu.app.components.bulletinBoard.common.models.UserGroupStorage.makeStaticAudience
import org.astu.app.components.bulletinBoard.common.models.UserStorage
import org.astu.app.components.bulletinBoard.common.models.UserStorage.makeStaticUserText
import org.astu.app.components.bulletinBoard.common.models.UserSummary

class AnnouncementDetailsScreen(private val onReturn: () -> Unit) : Screen {
    @Composable
    override fun Content() {
        AnnouncementActionScreenScaffold(
            onReturn = onReturn,
            topBarTitle = { Text("Объявление") },
        ) {
            AnnouncementDetails(constructAnnouncementDetails())
        }
    }

    @Composable
    private fun constructAnnouncementDetails(): AnnouncementDetailsContent {
        val attachments = makeAttachments()
        val rootAudienceGroup = makeStaticAudience()

        return AnnouncementDetailsContent(
            id = uuid4(),
            author = "Белов Сергей Валерьевич",
            publicationTime = "Опубликовано 15 фев 15:50",
            viewed = 145,
            viewedPercent = 48,
            audienceSize = 300,
            text = "Lorem ipsum dolor sit amet consectetur. Risus nunc aliquam ipsum mollis fames rhoncus lobortis integer mauris. Ut urna sem quis mi elementum suscipit donec.",
            rootAudienceNode = rootAudienceGroup,
            attachments = attachments
        )
    }

    private fun makeAttachments(): List<AttachmentBase> {
        val file1 = FileSummary("Документ.docx", "20 мб", mutableStateOf(FileDownloadState.NOT_DOWNLOADED))
        val file2 = FileSummary("Презентация.pptx", "20 мб", mutableStateOf(FileDownloadState.DOWNLOADING))
        val file3 = FileSummary("Таблица.xlsx", "20 мб", mutableStateOf(FileDownloadState.DOWNLOADED))

        val user1 = UserStorage.user1
        val user2 = UserStorage.user2
        val user3 = UserStorage.user3
        val user4 = UserStorage.user4
        val user5 = UserStorage.user5
        val user6 = UserStorage.user6

        val answers = listOf(
            VotedAnswerContentDetails("Ответ 1 на вопрос с единственным выбором", 33, makeVoters(listOf(user1, user2))),
            VotedAnswerContentDetails("Ответ 2 на вопрос с единственным выбором", 17, makeVoters(listOf(user3))),
            VotedAnswerContentDetails("Ответ 3 на вопрос с единственным выбором", 50, makeVoters(listOf(user4, user5, user6))),
        )
        val questions = listOf(
            VotedQuestionContent("Вопрос с единственным выбором", answers)
        )
        val survey = SurveyContent(questions)

        return listOf(file1, file2, file3, survey)
    }

    private fun makeVoters(voters: List<UserSummary>): INode {
        val votersAsTreeLeafs = voters.map { voter -> Leaf(makeStaticUserText(voter, Modifier.padding(start = 16.dp))) }
        return Node(votersAsTreeLeafs, content = {})
    }
}