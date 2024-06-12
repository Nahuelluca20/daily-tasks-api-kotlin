package com.dailytasksbe.dailytasksbe.service

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.jdbc.core.query
import com.dailytasksbe.dailytasksbe.dto.Collection
import com.dailytasksbe.dailytasksbe.dto.UpdatedCollection
import org.springframework.dao.DataAccessException
import java.sql.ResultSet
import java.util.*
import kotlin.reflect.full.memberProperties

@Service
class CollectionService(val db: JdbcTemplate) {
    fun findCollections(): List<Collection> = db.query("SELECT * FROM collections") { rs, _ ->
        mapRowToCollection(rs)
    }

    fun findTasksInCollectionById(collectionId: UUID): Collection? {
        val collections = db.query("SELECT * FROM collections WHERE id = ?", collectionId) { rs, _ ->
            mapRowToCollection(rs)
        }

        return collections.firstOrNull()
    }

    fun postCollection(collection: Collection) {
        db.update(
            "INSERT INTO collections (name, banner, user_id) VALUES (?, ?, ?)",
            collection.name, collection.banner, collection.userId
        )
    }

    fun updateCollection(collection: UpdatedCollection, id: UUID?): Int? {
        if (id == null || id.toString().length < 0) {
            return null
        }

        var addToQuery = "UPDATE collections SET "
        val addValues = mutableListOf<Any?>()

        for (prop in UpdatedCollection::class.memberProperties) {
            val value = prop.get(collection)
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

    private fun mapRowToCollection(rs: ResultSet): Collection {
        return Collection(
            UUID.fromString(rs.getString("id")),
            rs.getString("name"),
            rs.getString("banner"),
            UUID.fromString(rs.getString("user_id")),
        )
    }
}