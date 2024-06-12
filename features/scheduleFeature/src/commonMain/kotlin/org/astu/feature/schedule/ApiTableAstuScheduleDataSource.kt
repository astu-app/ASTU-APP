package org.astu.feature.schedule

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.astu.feature.schedule.entities.*
import kotlinx.serialization.*
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable

class ApiTableAstuScheduleDataSource : ScheduleDataSource, JavaSerializable {
    private val client by GlobalDIContext.inject<HttpClient>()

    override suspend fun find(value: String): List<SearchResult> {
        val response = client.get("https://apitable.astu.org/search/find") {
            parameter("q", value)
        }

        return response.body<Array<SearchItem>>().map { search ->
            SearchResult("", search.name, search.type.toSearchType())
        }
    }

    private fun String.toSearchType(): SearchType {
        return when (this) {
            "teacher" -> SearchType.Teacher
            "audience" -> SearchType.Auditory
            "group" -> SearchType.Group
            else -> TODO()
        }
    }

    private fun SearchType.toStringType(): String {
        return when (this) {
            SearchType.Teacher -> "teacher"
            SearchType.Auditory ->  "audience"
            SearchType.Group ->  "group"
        }
    }

    override suspend fun getTerm(searchResult: SearchResult): Term {
        val response = client.get("https://apitable.astu.org/search/get") {
            parameter("q", searchResult.name)
            parameter("t", searchResult.type.toStringType())
        }
        return response.body<ClassSchedule>().toTerm()
    }

    override suspend fun getTerm(teacher: Teacher): Term {
        val response = client.get("https://apitable.astu.org/search/get") {
            parameter("q", teacher.name)
            parameter("t", "teacher")
        }
        return response.body<ClassSchedule>().toTerm()
    }

    override suspend fun getTerm(groupOfStudents: GroupOfStudents): Term {
        val response = client.get("https://apitable.astu.org/search/get") {
            parameter("q", groupOfStudents.name)
            parameter("t", "group")
        }
        return response.body<ClassSchedule>().toTerm()
    }

    override suspend fun getTerm(auditory: Auditory): Term {
        val response = client.get("https://apitable.astu.org/search/get") {
            parameter("q", auditory.name)
            parameter("t", "audience")
        }
        return response.body<ClassSchedule>().toTerm()
    }

    private fun lessonOrderToTimeInterval(lessonOrderId: Int): TimeInterval {
        return when (lessonOrderId) {
            0 -> TimeInterval("1", "8:30", "10:00")
            1 -> TimeInterval("2", "10:15", "11:45")
            2 -> TimeInterval("3", "12:00", "13:30")
            3 -> TimeInterval("4", "14:00", "15:30")
            4 -> TimeInterval("5", "15:45", "17:15")
            5 -> TimeInterval("6", "17:30", "19:00")
            6 -> TimeInterval("7", "19:15", "20:45")
            else -> TODO()
        }
    }

    private fun ClassSchedule.toTerm(): Term = Term(
        this.name,
        this.type.toSearchType(),
        this.lessons.map { lesson ->
            Class(
                interval = lessonOrderToTimeInterval(lesson.lessonOrderId),
                dayOfWeek = dayIdToDayOfWeek(lesson.dayId),
                isEvenWeek = lesson.dayId >= 6,
                auditory = Auditory("", lesson.entries.first().audience ?: this.name),
                classType = infoToClassType(lesson.entries.first().type),
                discipline = Discipline("", lesson.entries.first().discipline),
                teacher = Teacher("", lesson.entries.first().teacher ?: this.name),
                groupsOfStudents = lesson.entries.first().groups?.map { GroupOfStudents("", it) } ?: listOf()
            )
        })

    private fun dayIdToDayOfWeek(dayId: Int): DayOfWeek {
        return when (dayId % 6) {
            0 -> DayOfWeek.Monday
            1 -> DayOfWeek.Tuesday
            2 -> DayOfWeek.Wednesday
            3 -> DayOfWeek.Thursday
            4 -> DayOfWeek.Friday
            5 -> DayOfWeek.Saturday
            6 -> DayOfWeek.Sunday
            else -> TODO()
        }
    }

    private fun infoToClassType(type: String): ClassType {
        return when (type) {
            "laboratory" -> ClassType.Laboratory
            "lecture" -> ClassType.Lecture
            "practice" -> ClassType.Practice
            else -> TODO()
        }
    }

    @Serializable
    private data class SearchItem(val name: String, val type: String)

    @Serializable
    private data class ClassSchedule(val id: String, val name: String, val type: String, val lessons: List<Lesson>)

    @Serializable
    private data class Lesson(val dayId: Int, val lessonOrderId: Int, val entries: List<Info>)

    @Serializable
    private data class Info(
        val teacher: String? = null,
        val audience: String? = null,
        val groups: List<String>? = null,
        val discipline: String,
        val type: String
    )
}