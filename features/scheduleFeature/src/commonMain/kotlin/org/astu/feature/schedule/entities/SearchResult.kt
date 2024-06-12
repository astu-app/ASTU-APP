package org.astu.feature.schedule.entities

import org.astu.infrastructure.JavaSerializable

enum class SearchType: JavaSerializable {
    Teacher,
    Group,
    Auditory
}

data class SearchResult(val id: String, val name: String, val type: SearchType): JavaSerializable