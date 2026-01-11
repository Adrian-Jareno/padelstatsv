package com.example.padelstats.ui.screens.matches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.padelstats.data.entity.MatchUiModel
import com.example.padelstats.data.entity.PlayerEntity
import com.example.padelstats.data.repo.PadelRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MatchesViewModel(
    private val repo: PadelRepository
) : ViewModel() {

    val matches: StateFlow<List<MatchUiModel>> =
        repo.observeMatches()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val players: StateFlow<List<PlayerEntity>> =
        repo.observePlayers()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun createMatch(
        location: String,
        notes: String,
        teamAScore: Int,
        teamBScore: Int,
        participants: List<Pair<Long, String>>
    ) {
        viewModelScope.launch {
            repo.addMatch(location, notes, teamAScore, teamBScore, participants)
        }
    }

    fun deleteMatch(matchId: Long) {
        viewModelScope.launch {
            repo.deleteMatch(matchId)
        }
    }

    companion object {
        fun factory(repo: PadelRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MatchesViewModel(repo) as T
                }
            }
    }
}
