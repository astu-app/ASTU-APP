package org.astu.feature.single_window.entities

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4

data class AddRequirementField(val id: Uuid = uuid4(), val name: String = "", val description: String = "")