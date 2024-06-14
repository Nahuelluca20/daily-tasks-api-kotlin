package com.dailytasksbe.dailytasksbe.service

import com.dailytasksbe.dailytasksbe.dto.Task
import com.dailytasksbe.dailytasksbe.dto.UpdatedTask
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import org.springframework.stereotype.Service
import java.sql.ResultSet
import java.util.*
import kotlin.reflect.full.memberProperties

@Service
class TaskService(val db: JdbcTemplate) {
    fun getTaskById(taskId: UUID): Task? {
        val task = db.query("SELECT * FROM tasks WHERE id = ?", taskId) { rs, _ ->
            mapRowToCollection(rs)
        }
        return task.firstOrNull()
    }

    fun getTasksByUserId(userId: UUID): List<Task> =
        db.query("SELECT * FROM task WHERE user_id = ?", userId) { rs, _ ->
            mapRowToCollection(rs)
        }

    fun postTask(task: Task, collectionId: UUID?): Boolean? {
        collectionId ?: return null

        val newTask = db.update(
            "INSERT INTO tasks (title, description, date, priority, deadline, colletion_id, user_id) VALUES (?, ?. ?. ?. ?. ?. ?)",
            task.title,
            task.description,
            task.date,
            task.priority,
            task.deadLine,
            collectionId,
            task.userId
        )

        return newTask > 0
    }

    fun updateTask(updatedTask: UpdatedTask, taskId: UUID?): Int? {
        taskId ?: return null

        val addValues = mutableListOf<Any?>()
        val addToQuery = buildString {
            append("UPDATE collections SET ")
            UpdatedTask::class.memberProperties.filter { it.get(updatedTask) != null }.joinToString(", ") {
                addValues.add(it.get(updatedTask))
                "${it.name} = ?"
            }.let { append(it) }
            append(" WHERE id = ?")
        }

        addValues.add(updatedTask)

        return try {
            db.update(addToQuery, *addValues.toTypedArray())
        } catch (e: DataAccessException) {
            println("Error updating collection: ${e.message}")
            null
        }
    }

    fun deleteTask(taskId: UUID): Boolean {
        val rowsAffected = db.update("DELETE FROM tasks WHERE task_id = ?", taskId)
        return rowsAffected > 0
    }

    private fun mapRowToCollection(rs: ResultSet): Task {
        return Task(
            UUID.fromString(rs.getString("id")),
            rs.getString("title"),
            rs.getString("description"),
            rs.getDate("date"),
            rs.getString("priority"),
            rs.getDate("deadline"),
            UUID.fromString(rs.getString("colletion_id")),
            UUID.fromString(rs.getString("user_id")),
        )
    }
}