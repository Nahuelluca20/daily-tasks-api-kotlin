package com.dailytasksbe.dailytasksbe.service

import com.dailytasksbe.dailytasksbe.dto.UpdatedUser
import com.dailytasksbe.dailytasksbe.dto.User
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import org.springframework.stereotype.Service
import java.sql.ResultSet
import java.util.*
import kotlin.reflect.full.memberProperties

@Service
class UserService(val db: JdbcTemplate) {
    fun findUsers(): List<User> = db.query("SELECT * FROM users") { rs, _ ->
        mapRowToCollection(rs)
    }

    fun findUserById(userId: UUID): User? {
        val users = db.query("SELECT * FROM users WHERE id = ?", userId) { rs, _ ->
            mapRowToCollection(rs)
        }

        return users.firstOrNull()
    }

    fun postUser(user: User) {
        val id = user.id ?: UUID.randomUUID()
        db.update(
            "INSERT INTO users VALUES ( ?, ?, ?, ?, ?, ? )",
            id, user.name, user.username, user.age, user.email, user.password,
        )
    }

    fun updateUser(user: UpdatedUser, id: UUID?): Int? {
        id ?: return null

        val addValues = mutableListOf<Any?>()
        val addToQuery = buildString {
            append("UPDATE users SET ")
            UpdatedUser::class.memberProperties.filter { it.get(user) != null }.joinToString(", ") {
                addValues.add(it.get(user))
                "${it.name} = ?"
            }.let { append(it) }
            append(" WHERE id = ?")
        }

        addValues.add(id)

        return try {
            db.update(addToQuery, *addValues.toTypedArray())
        } catch (e: DataAccessException) {
            println("Error updating user: ${e.message}")
            null
        }
    }

    private fun mapRowToCollection(rs: ResultSet): User {
        return  User(
            UUID.fromString(rs.getString("id")),
            rs.getString("name"),
            rs.getString("username"),
            rs.getInt("age"),
            rs.getString("email"),
            rs.getString("password")
        )
    }
}