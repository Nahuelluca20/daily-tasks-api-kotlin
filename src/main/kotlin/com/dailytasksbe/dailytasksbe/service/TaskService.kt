package com.dailytasksbe.dailytasksbe.service

import com.dailytasksbe.dailytasksbe.dto.GetTask
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
    fun getTaskById(taskId: UUID): GetTask? {
        val task = db.query("SELECT id, title, description, date, priority, deadline FROM tasks WHERE id = ?", taskId) { rs, _ ->
            mapRowToCollection(rs)
        }
        return task.firstOrNull()
    }

    fun getTasksByUserId(userId: UUID): List<GetTask> =
        db.query("SELECT id, title, description, date, priority, deadline FROM tasks WHERE user_id = ?", userId) { rs, _ ->
            mapRowToCollection(rs)
        }

    fun postTask(task: Task): Boolean? {

        val newTask = db.update(
            "INSERT INTO tasks (title, description, date, priority, deadline, collection_id, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)",
            task.title,
            task.description,
            task.date,
            task.priority,
            task.deadLine,
            task.collectionId,
            task.userId
        )

        return newTask > 0
    }

    fun updateTask(updatedTask: UpdatedTask, taskId: UUID?): Int? {
        taskId ?: return null

        val addValues = mutableListOf<Any?>()
        val addToQuery = buildString {
            append("UPDATE tasks SET ")
            UpdatedTask::class.memberProperties.filter { it.get(updatedTask) != null }.joinToString(", ") {
                addValues.add(it.get(updatedTask))
                "${it.name} = ?"
            }.let { append(it) }
            append(" WHERE id = ?")
        }

        addValues.add(taskId)

        return try {
            db.update(addToQuery, *addValues.toTypedArray())
        } catch (e: DataAccessException) {
            println("Error updating tasks: ${e.message}")
            null
        }
    }

    fun deleteTask(taskId: UUID): Boolean {
        val rowsAffected = db.update("DELETE FROM tasks WHERE task_id = ?", taskId)
        return rowsAffected > 0
    }

    private fun mapRowToCollection(rs: ResultSet): GetTask {
        return GetTask(
            UUID.fromString(rs.getString("id")),
            rs.getString("title"),
            rs.getString("description"),
            rs.getDate("date"),
            rs.getString("priority"),
            rs.getDate("deadline")
        )
    }
}