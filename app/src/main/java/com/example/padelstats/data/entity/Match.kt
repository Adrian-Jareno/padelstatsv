package com.example.padelstats.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matches")
data class MatchEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val dateEpoch: Long = System.currentTimeMillis(),
    val location: String,
    val teamAScore: Int,
    val teamBScore: Int,
    val notes: String = ""
)
