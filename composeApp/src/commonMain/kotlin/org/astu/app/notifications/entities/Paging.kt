package org.astu.app.notifications.entities

import kotlinx.serialization.Serializable
import org.astu.infrastructure.JavaSerializable

@Suppress("unused")
@Serializable
class Paging (val size: Int, val since: Int, val limit: Int, ): JavaSerializable