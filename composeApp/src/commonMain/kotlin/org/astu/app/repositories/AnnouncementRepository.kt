package org.astu.app.repositories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.benasher44.uuid.Uuid
import org.astu.app.components.bulletinBoard.announcements.details.models.AnnouncementDetailsContent
import org.astu.app.components.bulletinBoard.announcements.details.models.audienceGraph.Leaf
import org.astu.app.components.bulletinBoard.announcements.details.models.audienceGraph.Node
import org.astu.app.components.bulletinBoard.announcements.details.models.audienceGraph.NodeBase
import org.astu.app.components.bulletinBoard.attachments.common.models.AttachmentBase
import org.astu.app.components.bulletinBoard.attachments.files.models.FileDownloadState
import org.astu.app.components.bulletinBoard.attachments.files.models.FileSummary
import org.astu.app.components.bulletinBoard.common.models.UserStorage
import org.astu.app.components.bulletinBoard.common.models.UserSummary

class AnnouncementRepository {
    fun loadDetails(id: Uuid): AnnouncementDetailsContent {
        return AnnouncementDetailsContent(
            id = id,
            author = "Белов Сергей Валерьевич",
            publicationTime = "Опубликовано 15 фев 15:50",
            viewed = 145,
            viewedPercent = 48,
            audienceSize = 300,
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ",
            rootAudienceNode = makeAudience(),
            attachments = makeAttachments(),
        )
    }

    fun create() {

    }

    fun update() {

    }




    private fun makeAudience(): NodeBase {
        /*
        Структура групп:
        Группа 1
        + -- Группа 2
        |    + -- Анисимова Анна Максимовна
        |    + -- Романова Милана Матвеевна
        + -- Группа 3
             + -- Группа 4
             |    + -- Фадеев Максим Михайлович
             |    + -- Покровская Анастасия Андреевна
             |    + -- Воробьева Софья Дмитриевна
             + -- Группа 5
                  + -- Левин Тимофей Владимирович
         */
        val user1 = UserStorage.user1
        val user2 = UserStorage.user2
        val user3 = UserStorage.user3
        val user4 = UserStorage.user4
        val user5 = UserStorage.user5
        val user6 = UserStorage.user6

        val group2 = Node(listOf(Leaf(makeUserText(user1)), Leaf(makeUserText(user2))), makeGroupText("Группа 2"))
        val group4 = Node(
            listOf(Leaf(makeUserText(user3)), Leaf(makeUserText(user4)), Leaf(makeUserText(user5))),
            makeGroupText("Группа 4")
        )
        val group5 = Node(listOf(Leaf(makeUserText(user6))), makeGroupText("Группа 5"))
        val group3 = Node(listOf(group4, group5), makeGroupText("Группа 3"))
        val group1 = Node(listOf(group2, group3), makeGroupText("Группа 1"))

        return group1
    }

    private fun makeGroupText(text: String): @Composable () -> Unit {
        return {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            )
        }
    }

    private fun makeUserText(user: UserSummary, modifier: Modifier = Modifier): @Composable () -> Unit {
        return {
            Column(modifier = modifier) {
                Text(
                    text = user.firstName
                )
                val secondPartOfName =
                    if (user.patronymic != null)
                        "${user.secondName} ${user.patronymic}"
                    else user.secondName
                Text(
                    text = secondPartOfName,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }

    private fun makeAttachments(): List<AttachmentBase> {
        val attachments = mutableListOf<AttachmentBase>(
            FileSummary("Документ.docx", "20 мб", mutableStateOf(FileDownloadState.DOWNLOADED)),
            FileSummary("Презентация.pptx", "20 мб", mutableStateOf(FileDownloadState.DOWNLOADING)),
            FileSummary("Таблица.xlsx", "20 мб", mutableStateOf(FileDownloadState.NOT_DOWNLOADED)),
        )

        return attachments
    }
}