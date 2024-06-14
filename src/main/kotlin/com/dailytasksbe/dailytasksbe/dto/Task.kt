package com.dailytasksbe.dailytasksbe.dto

import java.util.*

/*Format Date is ISO 8601*/

data class Task(
    val id: UUID? = null,
    val title: String,
    val description: String,
    val date: Date,
    val priority: String,
    val deadLine: Date,
    val collectionId: UUID,
    val userId: UUID
)

data class GetTask(
    val id: UUID? = null,
    val title: String,
    val description: String,
    val date: Date,
    val priority: String,
    val deadLine: Date,
)

data class UpdatedTask(
    val id: UUID? = null,
    val title: String?,
    val description: String?,
    val date: Date?,
    val priority: String?,
    val deadLine: Date?,
    val collectionId: UUID?,
    val userId: UUID?
)

