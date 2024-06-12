package org.astu.feature.schedule.entities

import org.astu.infrastructure.JavaSerializable

class Term(val who: String, val type: SearchType, val classes: List<Class> = listOf()): JavaSerializable