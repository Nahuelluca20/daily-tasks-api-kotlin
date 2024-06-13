package com.dailytasksbe.dailytasksbe.dto

import java.util.*

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

