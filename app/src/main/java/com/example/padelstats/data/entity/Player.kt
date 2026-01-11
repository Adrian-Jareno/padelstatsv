package com.example.padelstats.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val level: String,     // Bajo / Intermedio / Alto
    val position: String   // Drive / Reves
)
