package com.dailytasksbe.dailytasksbe.service

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.jdbc.core.query
import com.dailytasksbe.dailytasksbe.dto.Collection
import com.dailytasksbe.dailytasksbe.dto.User
import java.sql.ResultSet
import java.util.*

@Service
class CollectionService(val db: JdbcTemplate) {
    fun findCollections(): List<Collection> = db.query("SELECT * FROM collections") { rs, _ ->
        mapRowToCollection(rs)
    }

    fun findCollectionById(collectionId: UUID): Collection? {
        val users = db.query("SELECT * FROM collections WHERE id = ?", collectionId) { rs, _ ->
            mapRowToCollection(rs)
        }

        return users.firstOrNull()
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