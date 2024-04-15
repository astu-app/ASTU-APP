package org.astu.app.view_models.bulletInBoard

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.StateScreenModel
import com.benasher44.uuid.Uuid
import org.astu.app.components.bulletinBoard.attachments.common.models.AttachmentBase
import org.astu.app.components.bulletinBoard.attachments.files.models.FileDownloadState
import org.astu.app.components.bulletinBoard.attachments.files.models.FileSummary
import org.astu.app.components.bulletinBoard.attachments.surveys.answers.models.VotedAnswerContentDetails
import org.astu.app.components.bulletinBoard.attachments.surveys.common.models.SurveyContent
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.QuestionContentBase
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.VotedQuestionContent
import org.astu.app.components.bulletinBoard.common.models.UserSummary
import org.astu.app.entities.bulletInBoard.announcement.details.AnnouncementDetailsContent
import org.astu.app.models.bulletInBoard.AnnouncementModel
import org.astu.app.models.bulletInBoard.entities.announcements.AnnouncementDetails
import org.astu.app.models.bulletInBoard.entities.attachments.file.File
import org.astu.app.models.bulletInBoard.entities.attachments.survey.details.AnswerDetails
import org.astu.app.models.bulletInBoard.entities.attachments.survey.details.QuestionDetails
import org.astu.app.models.bulletInBoard.entities.attachments.survey.details.SurveyDetails
import org.astu.app.models.bulletInBoard.entities.audience.User
import org.astu.app.view_models.bulletInBoard.humanization.humanizeFileSize
import kotlin.math.roundToInt

class AnnouncementDetailsViewModel (
    private val announcementId: Uuid
) : StateScreenModel<AnnouncementDetailsViewModel.State>(State.Loading) {
    sealed class State {
        data object Loading : State()
        data object LoadingDone : State()
        data object Error : State()
    }

    private val model: AnnouncementModel = AnnouncementModel()
    lateinit var content: AnnouncementDetailsContent


    init {
        mutableState.value = State.Loading
        loadDetails() // todo
    }

    private fun loadDetails() {
        content = toViewModel(model.getDetails(announcementId))
//        content = AnnouncementDetailsContent( // remove
//            id = uuid4(),
//            author = "Белов Сергей Валерьевич",
//            publicationTime = "Опубликовано 15 фев 15:50",
//            viewed = 145,
//            viewedPercent = 48,
//            audienceSize = 300,
//            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ",
//            rootAudienceNode = makeStaticAudience(),
////            attachments = makeAttachments(),
//        );
        mutableState.value = State.LoadingDone
    }

    private fun toViewModel(details: AnnouncementDetails): AnnouncementDetailsContent {
        return AnnouncementDetailsContent(
            id = details.id,
            author = details.authorName,
            publicationTime = "15 фев в 15:00", // todo humanize
            viewed = details.viewsCount,
            viewedPercent = calculateViewsCountPercent(details.viewsCount, details.audienceSize),
            audienceSize = details.audienceSize,
            text = details.content,
//            rootAudienceNode = audienceToViewModel(details.audience), // remove
            attachments = attachmentsToViewModel(details.files, details.surveys, details.audienceSize),
            audience = audienceToViewModel(details.audience),
        )
    }

    private fun calculateViewsCountPercent(views: Int, audienceSize: Int): Int {
        return (views.toDouble() / audienceSize).roundToInt()
    }

//    private fun audienceToViewModel(node: IAudienceNode): INode {        // remove
//        if (node is User) {
//            return Leaf(UserStorage.makeStaticUserText(node.firstName, node.secondName, node.patronymic))
//        } else if (node is UserGroup) {
//            val childNodes = node.nodes.map { audienceToViewModel(it) }
//            return Node(childNodes, UserGroupStorage.makeStaticUserGroupText(node.name))
//        }
//        throw UnsupportedOperationException("Незарегистрированный тип узла дерева: ${node::class.qualifiedName}")
//    }

    private fun audienceToViewModel(audience: List<User>): List<UserSummary> {
        return audience.map { UserSummary(it.id, it.firstName, it.secondName, it.patronymic) }
    }

    private fun attachmentsToViewModel(files: List<File>, surveys: List<SurveyDetails>, audienceSize: Int): List<AttachmentBase> {
        val attachments = mutableListOf<AttachmentBase>()
        files.forEach {
            // todo получение состояния загрузки файла
            attachments.add(FileSummary(it.id, it.name, humanizeFileSize(it.sizeInBytes), mutableStateOf(FileDownloadState.DOWNLOADED)))
        }

        val survey = surveys.firstOrNull() ?: return attachments
        attachments.add(surveyToViewModel(survey, audienceSize))

        return attachments
    }

    private fun surveyToViewModel(survey: SurveyDetails, audienceSize: Int): SurveyContent {
        return SurveyContent(questionsToViewModel(survey.questions, audienceSize))
    }

    private fun questionsToViewModel(questions: List<QuestionDetails>, audienceSize: Int): List<QuestionContentBase> {
        return questions.map {
            VotedQuestionContent(
                it.content,
                answersToViewModel(it.answers, audienceSize)
            )
        }
    }

    private fun answersToViewModel(answers: List<AnswerDetails>, audienceSize: Int): List<VotedAnswerContentDetails> {
        return answers.map {
            VotedAnswerContentDetails(it.content, calculateViewsCountPercent(it.votersAmount, audienceSize), null)
        }
    }
}