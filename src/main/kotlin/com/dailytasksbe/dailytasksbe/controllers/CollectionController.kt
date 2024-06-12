package com.dailytasksbe.dailytasksbe.controllers

import com.dailytasksbe.dailytasksbe.dto.Collection
import com.dailytasksbe.dailytasksbe.dto.UpdatedCollection
import com.dailytasksbe.dailytasksbe.service.CollectionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class CollectionController(val service: CollectionService) {

    @GetMapping("/collections")
    fun index(): List<Collection> = service.findCollections()

    @GetMapping("/collections/{id}/tasks")
    fun getTasksInCollection(@PathVariable("id") id: UUID): ResponseEntity<Any> {
        return try {
            val collection = service.findTasksInCollectionById(id)
            if (collection != null) {
                ResponseEntity.ok(collection)
            } else {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Collection with id $id not found")
            }
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body("Invalid UUID format: $id")
        }
    }

    @PostMapping("/collections")
    fun postCollections(@RequestBody collection: Collection) {
        service.postCollection(collection)
    }

    @PutMapping("/collections/{id}")
    fun updateCollection(@RequestBody collection: UpdatedCollection, @PathVariable id: UUID): ResponseEntity<Any> {
        val collectionUpdated = service.updateCollection(collection, id)

        return if (collectionUpdated != null) {
            ResponseEntity.ok().body("Collection with id: $id updated")
        } else {
            ResponseEntity.badRequest().body("Failed to update user with id: $id")
        }
    }
}