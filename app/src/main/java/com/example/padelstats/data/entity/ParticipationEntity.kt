package com.example.padelstats.data.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "participations",
    primaryKeys = ["matchId", "playerId"],
    indices = [Index("matchId"), Index("playerId")]
)
data class ParticipationEntity(
    val matchId: Long,
    val playerId: Long,
    val team: String // "A" o "B"
)
