package com.dailytasksbe.dailytasksbe.service

import com.dailytasksbe.dailytasksbe.dto.UpdatedUser
import com.dailytasksbe.dailytasksbe.dto.User
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

    fun updateUser(user: UpdatedUser, id: UUID?): String? {
        if (id == null || id.toString().length < 0) {
            return null
        }

        var addToQuery = ""
        var addValues = arrayOf<Any?>()

        for (prop in UpdatedUser::class.memberProperties) {
            /*println("${prop.name} = ${prop.get(user)}")*/
            if (prop.get(user) != null) {
                addToQuery += "${prop.name} = ?"
                addValues += prop.get(user)
                println(addValues)
            }
        }

        /*val rowsUpdated = db.update(
            "INSERT INTO users VALUES ( ?, ?, ?, ?, ?, ? )",
            id, user.name, user.username, user.age, user.email, user.password
        )*/

        return user.name ?: ""
        /*return if (rowsUpdated > 0) user else null*/
    }
}


fun VerifyDataUpdated(updatedUser: UpdatedUser) {
    when {
        updatedUser.name != null -> print("hola")

    }
}
