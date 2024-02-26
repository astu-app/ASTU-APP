package org.astu.app.entities

enum class SearchType {
    Teacher,
    Group,
    Auditory
}

data class SearchResult(val id: String, val name: String, val type: SearchType)