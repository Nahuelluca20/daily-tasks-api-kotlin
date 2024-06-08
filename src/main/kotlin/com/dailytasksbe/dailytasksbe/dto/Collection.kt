package com.dailytasksbe.dailytasksbe.dto

import java.util.UUID

data class Collection(
    val id: UUID,
    val name: String,
    val banner: String,
    val userId: UUID,
)
