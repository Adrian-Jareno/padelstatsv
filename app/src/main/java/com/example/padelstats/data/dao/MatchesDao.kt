package com.example.padelstats.data.dao

import androidx.room.*
import com.example.padelstats.data.entity.MatchEntity
import com.example.padelstats.data.entity.ParticipationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchesDao {

    @Query("SELECT * FROM matches ORDER BY dateEpoch DESC")
    fun observeMatches(): Flow<List<MatchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: MatchEntity): Long

    @Query("DELETE FROM matches WHERE id = :matchId")
    suspend fun deleteMatch(matchId: Long)

    // Participations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipations(items: List<ParticipationEntity>)

    @Query("DELETE FROM participations WHERE matchId = :matchId")
    suspend fun deleteParticipationsForMatch(matchId: Long)

    @Query("SELECT * FROM participations WHERE matchId = :matchId")
    suspend fun getParticipationsForMatch(matchId: Long): List<ParticipationEntity>

    @Query("SELECT * FROM participations WHERE matchId IN (:matchIds)")
    suspend fun getParticipationsForMatches(matchIds: List<Long>): List<ParticipationEntity>
}
