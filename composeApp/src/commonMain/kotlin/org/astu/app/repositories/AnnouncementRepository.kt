package org.astu.app.repositories

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import com.benasher44.uuid.uuidFrom
import kotlinx.datetime.LocalDateTime
import org.astu.app.components.bulletinBoard.attachments.common.models.AttachmentBase
import org.astu.app.components.bulletinBoard.attachments.files.models.FileDownloadState
import org.astu.app.components.bulletinBoard.attachments.files.models.FileSummary
import org.astu.app.components.bulletinBoard.attachments.surveys.answers.models.MultipleChoiceAnswerContent
import org.astu.app.components.bulletinBoard.attachments.surveys.answers.models.SingleChoiceAnswerContent
import org.astu.app.components.bulletinBoard.attachments.surveys.answers.models.VotedAnswerContentDetails
import org.astu.app.components.bulletinBoard.attachments.surveys.answers.models.VotedAnswerContentSummary
import org.astu.app.components.bulletinBoard.attachments.surveys.common.models.SurveyContent
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.MultipleChoiceQuestionContent
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.QuestionContentBase
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.SingleChoiceQuestionContent
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.VotedQuestionContent
import org.astu.app.components.bulletinBoard.common.models.UserStorage
import org.astu.app.components.bulletinBoard.common.models.UserSummary
import org.astu.app.dataSources.bulletInBoard.announcements.published.ApiPublishedAnnouncementDataSource
import org.astu.app.entities.bulletInBoard.announcement.summary.AnnouncementSummaryContent
import org.astu.app.entities.bulletInBoard.audienceGraph.INode
import org.astu.app.entities.bulletInBoard.audienceGraph.Leaf
import org.astu.app.entities.bulletInBoard.audienceGraph.Node
import org.astu.app.models.bulletInBoard.entities.announcements.AnnouncementDetails
import org.astu.app.models.bulletInBoard.entities.announcements.CreateAnnouncement
import org.astu.app.models.bulletInBoard.entities.attachments.file.File
import org.astu.app.models.bulletInBoard.entities.attachments.file.FileType
import org.astu.app.models.bulletInBoard.entities.attachments.survey.details.AnswerDetails
import org.astu.app.models.bulletInBoard.entities.attachments.survey.details.QuestionDetails
import org.astu.app.models.bulletInBoard.entities.attachments.survey.details.SurveyDetails
import org.astu.app.models.bulletInBoard.entities.audience.IAudienceNode
import org.astu.app.models.bulletInBoard.entities.audience.User
import org.astu.app.models.bulletInBoard.entities.audience.UserGroup
import org.astu.app.view_models.bulletInBoard.humanization.humanizeFileSize
import kotlin.math.roundToInt

class AnnouncementRepository {
    private val publishedAnnouncementsSource = ApiPublishedAnnouncementDataSource()

    suspend fun loadList(): List<AnnouncementSummaryContent> {
        return publishedAnnouncementsSource.getList().map {
            AnnouncementSummaryContent(it.id, it.author, it.publicationTime, it.text, it.viewed, it.audienceSize, mapAttachments(it.files, it.surveys))
        }
//        return mutableListOf(
//            constructAnnouncementWithFiles(),
//            constructAnnouncementWithMultipleQuestionsSurvey(10),
//            constructAnnouncementWithFilesAndSurvey(),
//            constructAnnouncementWithVotedSurvey(),
//            constructAnnouncementWithSingleChoiceSurvey(),
//            constructAnnouncementWithMultipleChoiceSurvey(),
//            constructAnnouncementWithOnlyText(),
//        )
    }

    private fun mapAttachments(files: List<File>?, surveys: List<SurveyDetails>?): List<AttachmentBase> {
        val attachments = mutableListOf<AttachmentBase>()
        attachments.addAll(mapFiles(files))
        attachments.addAll(mapSurveys(surveys))

        return attachments
    }

    private fun mapFiles(files: List<File>?): List<AttachmentBase> {
        if (files == null) return emptyList()
        return files.map {
            FileSummary(it.id, it.name, humanizeFileSize(it.sizeInBytes), mutableStateOf(FileDownloadState.DOWNLOADED))
        }
    }

    private fun mapSurveys(surveys: List<SurveyDetails>?): List<AttachmentBase> {
        if (surveys == null) return emptyList()
        return surveys.map { SurveyContent(mapQuestions(it.questions, it.votersAmount, it.voteFinishedAt != null)) }
    }

    private fun mapQuestions(questions: List<QuestionDetails>, votersAmount: Int, voteFinished: Boolean): List<QuestionContentBase> {
        return questions.map { question ->
            if (voteFinished) {
                mapVotedQuestion(question, votersAmount)
            } else if (question.isMultipleChoiceAllowed) {
                mapMultipleChoiceQuestion(question)
            } else {
                mapSingleChoiceQuestion(question)
            }
        }
    }

    private fun mapVotedQuestion(question: QuestionDetails, votersAmount: Int): VotedQuestionContent {
        val answers = question.answers.map { answer -> VotedAnswerContentSummary(answer.content, (answer.votersAmount.toDouble() /  votersAmount).roundToInt()) }
        return VotedQuestionContent(question.content, answers)
    }

    private fun mapMultipleChoiceQuestion(question: QuestionDetails): MultipleChoiceQuestionContent {
        val answers = question.answers.map { answer ->
            MultipleChoiceAnswerContent(
                answer.content,
                mutableStateOf(false)
            )
        }
        return MultipleChoiceQuestionContent(question.content, answers)
    }

    private fun mapSingleChoiceQuestion(question: QuestionDetails): SingleChoiceQuestionContent {
        val answers = question.answers.map { answer -> SingleChoiceAnswerContent(answer.content) }
        return SingleChoiceQuestionContent(question.content, answers)
    }



    fun loadDetails(id: Uuid): AnnouncementDetails {
        return AnnouncementDetails(
            id = id,
            content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ",
            authorName = "Белов Сергей Валерьевич",
            viewsCount = 145,
            audienceSize = 300,
//            audience = makeAudience(),
            audience = makeAudienceList(),
            files = makeFiles(),
            surveys = makeSurveys(),
            publishedAt = null, // todo вывод этих полей
            hiddenAt = null,
            delayedHidingAt = null,
            delayedPublishingAt = null,
        )
    }

    fun create(announcement: CreateAnnouncement) {

    }

    fun update() {

    }



    ///////////////////////////////// LIST
    private fun constructAnnouncementWithFiles(): AnnouncementSummaryContent {
        return AnnouncementSummaryContent(
            id = uuidFrom("30220a72-1be1-48e4-8321-4e037cea89ab"),
            author = "Белов Сергей Валерьевич",
//            publicationTime = "15 фев 15:50",
            publicationTime = LocalDateTime(2024, 2, 15, 15, 50),
            text = "Текст объявления с документами",
            viewed = 145,
            audienceSize = 300,
            attachments = listOf(
                FileSummary(uuid4(), "Документ.docx", "20.0 МБ", mutableStateOf(FileDownloadState.NOT_DOWNLOADED)),
                FileSummary(uuid4(), "Презентация.pptx", "20.0 МБ", mutableStateOf(FileDownloadState.DOWNLOADING)),
                FileSummary(uuid4(), "Таблица.xlsx", "20.0 МБ", mutableStateOf(FileDownloadState.DOWNLOADED)),
            ),

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
//            publicationTime = "15 фев 15:50",
            publicationTime = LocalDateTime(2024, 2, 15, 15, 50),
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
//            publicationTime = "15 фев 15:50",
            publicationTime = LocalDateTime(2024, 2, 15, 15, 50),
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
//            publicationTime = "15 фев 15:50",
            publicationTime = LocalDateTime(2024, 2, 15, 15, 50),
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
//            publicationTime = "15 фев 15:50",
            publicationTime = LocalDateTime(2024, 2, 15, 15, 50),
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
//            publicationTime = "15 фев 15:50",
            publicationTime = LocalDateTime(2024, 2, 15, 15, 50),
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
            FileSummary(uuid4(), "Документ.docx", "20.0 МБ", mutableStateOf(FileDownloadState.DOWNLOADED)),
        )

        return AnnouncementSummaryContent(
            id = uuidFrom("a212918e-ce44-4fb2-bafa-4bc7bb3d9bd8"),
            author = "Белов Сергей Валерьевич",
//            publicationTime = "15 фев 15:50",
            publicationTime = LocalDateTime(2024, 2, 15, 15, 50),
            text = "Текст объявления с файлом и опросом с единственным выбором",
            viewed = 145,
            audienceSize = 300,
            attachments = attachments
        )
    }

    ///////////////////////////////// DETAILS
    private fun makeAudience(): IAudienceNode {
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

        val group2 = UserGroup(
            id = uuid4(),
            name = "Группа 2",
            nodes = listOf(
                User(user1.id, user1.firstName, user1.secondName, user1.patronymic),
                User(user2.id, user2.firstName, user2.secondName, user2.patronymic),
            )
        )
        val group4 = UserGroup(
            id = uuid4(),
            name = "Группа 4",
            nodes = listOf(
                User(user3.id, user3.firstName, user3.secondName, user3.patronymic),
                User(user4.id, user4.firstName, user4.secondName, user4.patronymic),
                User(user5.id, user5.firstName, user5.secondName, user5.patronymic),
            )
        )
        val group5 = UserGroup(
            id = uuid4(),
            name = "Группа 5",
            nodes = listOf(User(user6.id, user6.firstName, user6.secondName, user6.patronymic),)
        )
        val group3 = UserGroup(
            id = uuid4(),
            name = "Группа 3",
            nodes = listOf(group4, group5)
        )
        val group1 = UserGroup(
            id = uuid4(),
            name = "Группа 1",
            nodes = listOf(group2, group3)
        )

        return group1
    }

    private fun makeAudienceList(): List<User> {
        return listOf(
            User(uuid4(), UserStorage.user1.firstName, UserStorage.user1.secondName, UserStorage.user1.patronymic),
            User(uuid4(), UserStorage.user2.firstName, UserStorage.user2.secondName, UserStorage.user2.patronymic),
            User(uuid4(), UserStorage.user3.firstName, UserStorage.user3.secondName, UserStorage.user3.patronymic),
            User(uuid4(), UserStorage.user4.firstName, UserStorage.user4.secondName, UserStorage.user4.patronymic),
            User(uuid4(), UserStorage.user5.firstName, UserStorage.user5.secondName, UserStorage.user5.patronymic),
            User(uuid4(), UserStorage.user6.firstName, UserStorage.user6.secondName, UserStorage.user6.patronymic),
        )
    }

    private fun makeFiles(): List<File> {
        return listOf(
            File(uuid4(), "Документ.docx", FileType.FILE,  20_971_520),
            File(uuid4(), "Презентация.pptx", FileType.FILE,  20_971_520),
            File(uuid4(), "Таблица.xlsx", FileType.FILE,  20_971_520),
        )
    }

    private fun makeSurveys(): List<SurveyDetails> {
        val answers = listOf(
            AnswerDetails(uuid4(), "Ответ 1 на вопрос с единственным выбором", 33),
            AnswerDetails(uuid4(), "Ответ 2 на вопрос с единственным выбором", 17),
            AnswerDetails(uuid4(), "Ответ 3 на вопрос с единственным выбором", 50),
        )
        val questions = listOf(
            QuestionDetails(
                id = uuid4(),
                content = "Вопрос с единственным выбором",
                isMultipleChoiceAllowed = false,
                answers = answers
            )
        )
        val survey = SurveyDetails(uuid4(),
            isOpen = true,
            isAnonymous = false,
            autoClosingAt = null,
            voteFinishedAt = null,
            votersAmount = 125,
            questions = questions
        )

        return listOf(survey)
    }

    private fun makeAttachments(): List<AttachmentBase> {
        val file1 = FileSummary(uuid4(), "Документ.docx", "20.0 МБ", mutableStateOf(FileDownloadState.DOWNLOADED))
        val file2 = FileSummary(uuid4(), "Презентация.pptx", "20.0 МБ", mutableStateOf(FileDownloadState.DOWNLOADING))
        val file3 = FileSummary(uuid4(), "Таблица.xlsx", "20.0 МБ", mutableStateOf(FileDownloadState.NOT_DOWNLOADED))

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
        val votersAsTreeLeafs = voters.map { voter -> Leaf(UserStorage.makeStaticUserText(voter, Modifier.padding(start = 16.dp))) }
        return Node(votersAsTreeLeafs, content = {})
    }
}