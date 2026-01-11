package com.example.padelstats.ui.screens.matches

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.padelstats.data.entity.PlayerEntity

@Composable
fun MatchFormDialog(
    vm: MatchesViewModel,
    onDismiss: () -> Unit,
    onCreated: () -> Unit
) {
    val players by vm.players.collectAsState()

    var location by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var teamAScore by remember { mutableStateOf("0") }
    var teamBScore by remember { mutableStateOf("0") }

    // playerId -> team ("A"/"B")
    val selected = remember { mutableStateMapOf<Long, String>() }

    val canSave =
        location.trim().isNotEmpty() &&
                selected.size == 4 &&
                selected.values.count { it == "A" } == 2 &&
                selected.values.count { it == "B" } == 2 &&
                teamAScore.toIntOrNull() != null &&
                teamBScore.toIntOrNull() != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crear partido") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Ubicación") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = teamAScore,
                        onValueChange = { teamAScore = it.filter { ch -> ch.isDigit() }.take(2).ifBlank { "0" } },
                        label = { Text("Equipo A") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = teamBScore,
                        onValueChange = { teamBScore = it.filter { ch -> ch.isDigit() }.take(2).ifBlank { "0" } },
                        label = { Text("Equipo B") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notas (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Selecciona 4 jugadores (2 en A y 2 en B):", style = MaterialTheme.typography.titleSmall)

                if (players.isEmpty()) {
                    Text("No hay jugadores. Crea jugadores primero.")
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 260.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(players, key = { it.id }) { p ->
                            PlayerPickRow(
                                player = p,
                                selectedTeam = selected[p.id],
                                onSetTeam = { team ->
                                    if (team == null) selected.remove(p.id) else selected[p.id] = team
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = canSave,
                onClick = {
                    vm.createMatch(
                        location = location.trim(),
                        notes = notes.trim(),
                        teamAScore = teamAScore.toIntOrNull() ?: 0,
                        teamBScore = teamBScore.toIntOrNull() ?: 0,
                        participants = selected.map { it.key to it.value }
                    )
                    onCreated()
                }
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
private fun PlayerPickRow(
    player: PlayerEntity,
    selectedTeam: String?,
    onSetTeam: (String?) -> Unit
) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Text(player.name, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = selectedTeam == "A",
                    onClick = { onSetTeam(if (selectedTeam == "A") null else "A") },
                    label = { Text("Equipo A") }
                )
                FilterChip(
                    selected = selectedTeam == "B",
                    onClick = { onSetTeam(if (selectedTeam == "B") null else "B") },
                    label = { Text("Equipo B") }
                )
                if (selectedTeam != null) {
                    TextButton(onClick = { onSetTeam(null) }) { Text("Quitar") }
                }
            }
        }
    }
}
