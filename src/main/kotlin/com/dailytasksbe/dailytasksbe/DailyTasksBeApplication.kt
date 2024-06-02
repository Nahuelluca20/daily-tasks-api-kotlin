package com.dailytasksbe.dailytasksbe

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DailyTasksBeApplication

fun main(args: Array<String>) {
    runApplication<DailyTasksBeApplication>(*args)
}
