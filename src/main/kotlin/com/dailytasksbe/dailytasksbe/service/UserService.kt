package com.dailytasksbe.dailytasksbe.service

import com.dailytasksbe.dailytasksbe.dto.User
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(val db: JdbcTemplate) {
    fun findUsers(): List<User> = db.query("select * from users") { rs, _ ->
        User(
            UUID.fromString(rs.getString("id")),
            rs.getString("name"),
            rs.getInt("age").toString(),
            rs.getString("username"),
            rs.getString("email"),
            rs.getString("password")
        )
    }


    fun postUser(user: User) {
        val id = user.id ?: UUID.randomUUID()
        db.update(
            "INSERT INTO users VALUES ( ?, ?, ?, ?, ?, ? )",
            id, user.name, user.username, user.age, user.email, user.password,
        )
    }
}
