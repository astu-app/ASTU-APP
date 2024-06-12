package org.astu.feature.schedule.repositories

import org.astu.feature.schedule.ApiTableAstuScheduleDataSource
import org.astu.feature.schedule.ScheduleDataSource
import org.astu.feature.schedule.entities.*
import org.astu.infrastructure.JavaSerializable

class ScheduleRepository: JavaSerializable {
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