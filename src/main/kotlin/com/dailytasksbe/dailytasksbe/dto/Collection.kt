package com.dailytasksbe.dailytasksbe.dto

import java.util.UUID

data class Collection(
    val id: UUID? = null,
    val name: String,
    val banner: String,
    val userId: UUID,
)

data class UpdatedCollection(
    val id: UUID?,
    val name: String?,
    val banner: String?,
    val userId: UUID?,
)
