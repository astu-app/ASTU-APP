package org.astu.app

import org.astu.app.entities.*

interface ScheduleDataSource {
    suspend fun find(value: String): List<SearchResult>

    suspend fun getTerm(searchResult: SearchResult): Term

    suspend fun getTerm(teacher: Teacher): Term
    suspend fun getTerm(groupOfStudents: GroupOfStudents): Term
    suspend fun getTerm(auditory: Auditory): Term
}