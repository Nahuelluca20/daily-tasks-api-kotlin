package com.dailytasksbe.dailytasksbe.controllers

import com.dailytasksbe.dailytasksbe.dto.UpdatedUser
import com.dailytasksbe.dailytasksbe.dto.User
import com.dailytasksbe.dailytasksbe.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
class UserController(val service: UserService) {
    @GetMapping("users")
    fun index(): List<User> = service.findUsers()

    @PostMapping("users")
    fun post(@RequestBody user: User) {
        service.postUser(user)
    }

    @GetMapping("users/{userId}")
    fun getUserById(@PathVariable userId: String): ResponseEntity<Any> {
        return try {
            val uuid = UUID.fromString(userId)
            val user = service.findUserById(uuid)
            if (user != null) {
                ResponseEntity.ok(user)
            } else {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id $userId not found")
            }
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body("Invalid UUID format: $userId")
        }
    }


    @PutMapping("users/{id}")
    fun update(@RequestBody user: UpdatedUser, @PathVariable id: UUID): ResponseEntity<Any> {
        val userUpdated = service.updateUser(user, id)
        return if (userUpdated != null) {
            ResponseEntity.ok().body("User with id: $id updated")
        } else {
            ResponseEntity.badRequest().body("Failed to update user with id: $id")
        }
    }
}











