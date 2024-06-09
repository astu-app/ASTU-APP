package org.astu.feature.single_window.entities

import kotlinx.serialization.Serializable

@Serializable
class RequirementField<T>(val requirement: Requirement, var value: T)

