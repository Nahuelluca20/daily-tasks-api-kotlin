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

    fun findInCollectionById(collectionId: UUID): Collection? {
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
        id ?: return null

        val addValues = mutableListOf<Any?>()
        val addToQuery = buildString {
            append("UPDATE collections SET ")
            UpdatedCollection::class.memberProperties.filter { it.get(collection) != null }.joinToString(", ") {
                addValues.add(it.get(collection))
                "${it.name} = ?"
            }.let { append(it) }
            append(" WHERE id = ?")
        }

        addValues.add(id)

        return try {
            db.update(addToQuery, *addValues.toTypedArray())
        } catch (e: DataAccessException) {
            println("Error updating collection: ${e.message}")
            null
        }
    }

    fun deleteCollectionById(collectionId: UUID): Boolean {
        val rowsAffected = db.update("DELETE FROM collections WHERE id = ?", collectionId)
        return rowsAffected > 0
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