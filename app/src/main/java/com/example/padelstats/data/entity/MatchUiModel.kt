package com.example.padelstats.data.entity

data class MatchParticipantUi(
    val playerId: Long,
    val name: String,
    val team: String
)

data class MatchUiModel(
    val id: Long,
    val dateEpoch: Long,
    val location: String,
    val teamAScore: Int,
    val teamBScore: Int,
    val notes: String,
    val participants: List<MatchParticipantUi>
)
