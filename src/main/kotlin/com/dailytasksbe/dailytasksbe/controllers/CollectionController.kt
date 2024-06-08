package com.dailytasksbe.dailytasksbe.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class CollectionController() {

    @GetMapping("/collections")
    fun index(): String {
        return "collections"
    }

    @GetMapping("/collections/{id}/tasks")
    fun getTasksInCollection(@PathVariable("id") id: UUID): String {
        return "tasks In Collection with id: $id"
    }

    @PostMapping("/collections")
    fun postCollections(): String {
        return "post collection"
    }

    @PutMapping("/collections/{id}")
    fun updateCollection(@PathVariable("id") id: UUID): String {
        return "updated collection $id"
    }
}