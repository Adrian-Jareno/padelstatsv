package com.example.padelstats.data.repo

import android.content.Context
import com.example.padelstats.data.db.AppDatabase

object AppGraph {

    lateinit var db: AppDatabase
        private set

    lateinit var repo: PadelRepository
        private set

    fun init(context: Context) {
        db = AppDatabase.build(context)

        repo = PadelRepository(
            playersDao = db.playersDao(),
            matchesDao = db.matchesDao()
        )
    }
}
