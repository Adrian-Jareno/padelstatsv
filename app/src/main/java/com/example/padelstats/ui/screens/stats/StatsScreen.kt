package com.example.padelstats.ui.screens.stats

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.padelstats.data.repo.PadelRepository

@Composable
fun StatsScreen(
    repo: PadelRepository,
    padding: PaddingValues
) {
    val matches by repo.observeMatches().collectAsState(initial = emptyList())
    val players by repo.observePlayers().collectAsState(initial = emptyList())

    val totalMatches = matches.size
    val totalPlayers = players.size

    val totalTeamAWins = matches.count { it.teamAScore > it.teamBScore }
    val totalTeamBWins = matches.count { it.teamBScore > it.teamAScore }
    val draws = matches.count { it.teamAScore == it.teamBScore }

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Estadísticas", style = MaterialTheme.typography.headlineSmall)

        ElevatedCard {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Jugadores: $totalPlayers", style = MaterialTheme.typography.titleMedium)
                Text("Partidos: $totalMatches", style = MaterialTheme.typography.titleMedium)
            }
        }

        ElevatedCard {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Victorias Equipo A: $totalTeamAWins")
                Text("Victorias Equipo B: $totalTeamBWins")
                Text("Empates: $draws")
            }
        }

        if (totalMatches == 0) {
            Text("Añade partidos para ver más estadísticas.", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
