package com.dailytasksbe.dailytasksbe.dto

data class User(
    val id: Long,
    val name: String,
    val username: String,
    val email: String,
    val password: String,
)