package com.example.padelstats.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.padelstats.data.dao.MatchesDao
import com.example.padelstats.data.dao.PlayersDao
import com.example.padelstats.data.entity.MatchEntity
import com.example.padelstats.data.entity.ParticipationEntity
import com.example.padelstats.data.entity.PlayerEntity

@Database(
    entities = [PlayerEntity::class, MatchEntity::class, ParticipationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun playersDao(): PlayersDao
    abstract fun matchesDao(): MatchesDao

    companion object {
        fun build(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "padelstats.db"
            ).fallbackToDestructiveMigration()
                .build()
        }
    }
}
