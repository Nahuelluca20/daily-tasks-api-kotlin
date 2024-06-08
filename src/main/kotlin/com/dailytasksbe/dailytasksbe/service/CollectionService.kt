package com.dailytasksbe.dailytasksbe.service

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class CollectionService(val db: JdbcTemplate) {

}