package com.dailytasksbe.dailytasksbe.controllers

import com.dailytasksbe.dailytasksbe.dto.GetTask
import com.dailytasksbe.dailytasksbe.dto.Task
import com.dailytasksbe.dailytasksbe.dto.UpdatedTask
import com.dailytasksbe.dailytasksbe.service.TaskService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
class TaskController(val service: TaskService) {
    @GetMapping("/tasks/{id}")
    fun getTaskById(@PathVariable("id") id: UUID): ResponseEntity<Any> {
        return try {
            val task = service.getTaskById(id)
            if (task != null) {
                ResponseEntity.ok(task)
            } else {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task with id $id not found")
            }
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body("Invalid UUID format: $id")
        }
    }

    @GetMapping("/tasks/users/{userId}")
    fun getTasksByUserId(@PathVariable("userId") userId: UUID): List<GetTask> = service.getTasksByUserId(userId)

    @PostMapping("/tasks/collection/{collectionId}")
    fun addTaskInCollection(
        @PathVariable("collectionId") collectionId: UUID,
        @RequestBody task: Task
    ): ResponseEntity<Any> {
        return try {
            val newTask = service.postTask(task)
            if (newTask != null) {
                ResponseEntity(HttpStatus.CREATED)
            } else {
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Task with id $task not found in collection $collectionId")
            }
        } catch (e: IllegalArgumentException) {
            ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        }
    }

    @PutMapping("/tasks/{taskId}")
    fun updateTaskInCollection(
        @PathVariable("taskId") taskId: UUID,
        @RequestBody task: UpdatedTask
    ): ResponseEntity<Any> {
        val tasksUpdated = service.updateTask(task, taskId)

        return if (tasksUpdated != null) {
            ResponseEntity.ok().body("Collection with id: $taskId updated")
        } else {
            ResponseEntity.badRequest().body("Failed to update collection with id: $taskId")
        }
    }

    @DeleteMapping("/tasks/{id}")
    fun deleteTaskById(@PathVariable("id") id: UUID): ResponseEntity<Any> {
        return if (service.deleteTask(id)) {
            ResponseEntity(HttpStatus.NO_CONTENT)
        } else {
            ResponseEntity("Collection with ID $id not found", HttpStatus.NOT_FOUND)
        }
    }
}