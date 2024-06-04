package com.dailytasksbe.dailytasksbe.dto

import java.util.*

data class User(
    val id: UUID?,
    val name: String,
    val username: String,
    val age: String,
    val email: String,
    val password: String,
)