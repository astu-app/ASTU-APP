package org.astu.app.notifications.entities

import kotlinx.serialization.Serializable

@Suppress("unused")
@Serializable
class Paging (val size: Int, val since: Int, val limit: Int, )