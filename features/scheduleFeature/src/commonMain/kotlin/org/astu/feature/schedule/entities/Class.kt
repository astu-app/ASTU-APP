package org.astu.feature.schedule.entities

import org.astu.feature.schedule.entities.*
import org.astu.infrastructure.JavaSerializable

class Class(
    val dayOfWeek: DayOfWeek,
    val classType: ClassType,
    val isEvenWeek: Boolean,
    val interval: TimeInterval,
    val auditory: Auditory,
    val teacher: Teacher,
    val discipline: Discipline,
    val groupsOfStudents: List<GroupOfStudents>
): JavaSerializable