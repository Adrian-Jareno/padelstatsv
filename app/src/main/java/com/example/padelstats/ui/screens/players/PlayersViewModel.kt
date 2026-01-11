package com.example.padelstats.ui.screens.players

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.padelstats.data.entity.PlayerEntity
import com.example.padelstats.data.repo.PadelRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlayersViewModel(
    private val repo: PadelRepository
) : ViewModel() {

    val players: StateFlow<List<PlayerEntity>> =
        repo.observePlayers()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun createPlayer(name: String, level: String, position: String) {
        viewModelScope.launch {
            repo.insertPlayer(
                PlayerEntity(
                    id = 0,
                    name = name,
                    level = level,
                    position = position
                )
            )
        }
    }

    fun updatePlayer(id: Long, name: String, level: String, position: String) {
        viewModelScope.launch {
            repo.upsertPlayer(
                PlayerEntity(
                    id = id,
                    name = name,
                    level = level,
                    position = position
                )
            )
        }
    }

    fun deletePlayer(id: Long) {
        viewModelScope.launch {
            repo.deletePlayer(id)
        }
    }

    companion object {
        fun factory(repo: PadelRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlayersViewModel(repo) as T
                }
            }
    }
}
