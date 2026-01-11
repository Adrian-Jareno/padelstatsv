package com.example.padelstats.data.repo

import com.example.padelstats.data.dao.MatchesDao
import com.example.padelstats.data.dao.PlayersDao
import com.example.padelstats.data.entity.MatchEntity
import com.example.padelstats.data.entity.MatchParticipantUi
import com.example.padelstats.data.entity.MatchUiModel
import com.example.padelstats.data.entity.ParticipationEntity
import com.example.padelstats.data.entity.PlayerEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class PadelRepository(
    private val playersDao: PlayersDao,
    private val matchesDao: MatchesDao
) {
    // ---------- PLAYERS ----------
    fun observePlayers(): Flow<List<PlayerEntity>> = playersDao.observePlayers()

    // Alias "insertPlayer" para que tu ViewModel no falle
    suspend fun insertPlayer(player: PlayerEntity) {
        playersDao.insert(
            player.copy(
                name = player.name.trim(),
                level = player.level.trim(),
                position = player.position.trim()
            )
        )
    }

    // Alias "upsertPlayer" (si id=0 -> insert; si no -> update)
    suspend fun upsertPlayer(player: PlayerEntity) {
        if (player.id == 0L) {
            insertPlayer(player)
        } else {
            playersDao.update(
                player.copy(
                    name = player.name.trim(),
                    level = player.level.trim(),
                    position = player.position.trim()
                )
            )
        }
    }

    suspend fun deletePlayer(id: Long) {
        playersDao.deleteById(id)
    }

    suspend fun getPlayer(id: Long): PlayerEntity? = playersDao.getById(id)

    // ---------- MATCHES ----------
    fun observeMatches(): Flow<List<MatchUiModel>> {
        return matchesDao.observeMatches()
            .combine(playersDao.observePlayers()) { matches, players ->
                val playersById = players.associateBy { it.id }
                val matchIds = matches.map { it.id }
                val parts = if (matchIds.isEmpty()) emptyList()
                else matchesDao.getParticipationsForMatches(matchIds)

                val partsByMatch = parts.groupBy { it.matchId }

                matches.map { m ->
                    val participants = partsByMatch[m.id].orEmpty().mapNotNull { p ->
                        val player = playersById[p.playerId] ?: return@mapNotNull null
                        MatchParticipantUi(
                            playerId = player.id,
                            name = player.name,
                            team = p.team
                        )
                    }

                    MatchUiModel(
                        id = m.id,
                        dateEpoch = m.dateEpoch,
                        location = m.location,
                        teamAScore = m.teamAScore,
                        teamBScore = m.teamBScore,
                        notes = m.notes,
                        participants = participants
                    )
                }
            }
    }

    suspend fun addMatch(
        location: String,
        notes: String,
        teamAScore: Int,
        teamBScore: Int,
        participants: List<Pair<Long, String>> // (playerId, "A"|"B")
    ) {
        val matchId = matchesDao.insertMatch(
            MatchEntity(
                location = location.trim(),
                notes = notes.trim(),
                teamAScore = teamAScore,
                teamBScore = teamBScore
            )
        )

        val participationRows = participants.map { (playerId, team) ->
            ParticipationEntity(matchId = matchId, playerId = playerId, team = team)
        }
        matchesDao.insertParticipations(participationRows)
    }

    suspend fun deleteMatch(matchId: Long) {
        matchesDao.deleteParticipationsForMatch(matchId)
        matchesDao.deleteMatch(matchId)
    }
}
