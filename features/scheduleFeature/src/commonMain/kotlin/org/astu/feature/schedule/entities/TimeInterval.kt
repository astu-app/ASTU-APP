package org.astu.feature.schedule.entities

import org.astu.infrastructure.JavaSerializable

data class TimeInterval(val number: String, val start: String, val end: String): JavaSerializable
