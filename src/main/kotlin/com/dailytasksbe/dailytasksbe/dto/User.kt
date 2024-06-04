package com.dailytasksbe.dailytasksbe.dto

import java.util.*

data class User(
    val id: UUID?,
    val name: String,
    val username: String,
    val age: Int,
    val email: String,
    val password: String,
)

data class UpdatedUser(
    val name: String,
    val username: String,
    val age: Int,
    val password: String,
    val email: String,
)