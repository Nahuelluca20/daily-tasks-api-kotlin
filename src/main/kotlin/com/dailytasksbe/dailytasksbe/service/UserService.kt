package com.dailytasksbe.dailytasksbe.service

import com.dailytasksbe.dailytasksbe.dto.User
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class UserService(private val jdbcTemplate: JdbcTemplate) {
    fun findUsers(): List<User> {
        return listOf(
            User(312321, "Nahuel", "nhl", 20, "nahuel4@luca.com", password = "dasdas"),
            User(312321, "Luca", "nhl_1", 20, "nahuel3@luca.com", password = "dasdasdas"),
            User(312321, "Fernanod", "nhl_2", 20, "nahuel2@luca.com", password = "dasdasdas")
        )
    }
}
