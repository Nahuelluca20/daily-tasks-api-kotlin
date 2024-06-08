package com.dailytasksbe.dailytasksbe.service

import com.dailytasksbe.dailytasksbe.dto.UpdatedUser
import com.dailytasksbe.dailytasksbe.dto.User
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import org.springframework.stereotype.Service
import java.util.*
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.internal.impl.util.ModuleVisibilityHelper.EMPTY


@Service
class UserService(val db: JdbcTemplate) {
    fun findUsers(): List<User> = db.query("SELECT * FROM users") { rs, _ ->
        User(
            UUID.fromString(rs.getString("id")),
            rs.getString("name"),
            rs.getString("username"),
            rs.getInt("age"),
            rs.getString("email"),
            rs.getString("password")
        )
    }

    fun findUserById(userId: UUID): User? {
        val users = db.query("SELECT * FROM users WHERE id = ?", userId) { rs, _ ->
            User(
                UUID.fromString(rs.getString("id")),
                rs.getString("name"),
                rs.getString("username"),
                rs.getInt("age"),
                rs.getString("email"),
                rs.getString("password")
            )
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
        if (id == null || id.toString().length < 0) {
            return null
        }

        var addToQuery = "UPDATE users SET "
        val addValues = mutableListOf<Any?>()

        for (prop in UpdatedUser::class.memberProperties) {
            val value = prop.get(user)
            if (value != null) {
                if (addValues.isNotEmpty()) {
                    addToQuery += ", "
                }
                addToQuery += "${prop.name} = ?"
                addValues.add(value)
            }
        }

        addToQuery += " WHERE id = ?"
        addValues.add(id)



        return try {
            val rowsAffected = db.update(addToQuery, *addValues.toTypedArray())
            rowsAffected
        } catch (e: DataAccessException) {
            println("Error updating user: ${e.message}")
            null
        }
    }
}