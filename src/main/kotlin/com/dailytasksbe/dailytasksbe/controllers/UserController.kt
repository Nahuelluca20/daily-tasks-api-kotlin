package com.dailytasksbe.dailytasksbe.controllers

import com.dailytasksbe.dailytasksbe.dto.User
import com.dailytasksbe.dailytasksbe.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(val service: UserService) {
    @GetMapping("/")
    fun index(): List<User> = service.findUsers()
}