package org.astu.app.repositories

import org.astu.app.ApiTableAstuScheduleDataSource
import org.astu.app.ScheduleDataSource
import org.astu.app.entities.*

class ScheduleRepository {
    private val scheduleDataSource: ScheduleDataSource = ApiTableAstuScheduleDataSource()

    suspend fun find(value: String): List<SearchResult> {
        if(value.isBlank())
            return emptyList()
        return scheduleDataSource.find(value)
    }

    suspend fun getTerm(searchResult: SearchResult): Term {
        return scheduleDataSource.getTerm(searchResult)
    }

    suspend fun getTerm(teacher: Teacher): Term {
        return scheduleDataSource.getTerm(teacher)
    }

    suspend fun getTerm(groupOfStudents: GroupOfStudents): Term {
        return scheduleDataSource.getTerm(groupOfStudents)
    }

    suspend fun getTerm(auditory: Auditory): Term {
        return scheduleDataSource.getTerm(auditory)
    }
}