package com.example.padelstats.data.dao

import androidx.room.*
import com.example.padelstats.data.entity.PlayerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayersDao {

    @Query("SELECT * FROM players ORDER BY name ASC")
    fun observePlayers(): Flow<List<PlayerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(player: PlayerEntity): Long

    @Update
    suspend fun update(player: PlayerEntity)

    @Query("DELETE FROM players WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM players WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): PlayerEntity?
}
