package com.example.padelstats.ui.screens.matches
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.padelstats.data.entity.MatchUiModel

@Composable
fun MatchesScreen(
    vm: MatchesViewModel,
    padding: PaddingValues
) {
    val matches by vm.matches.collectAsState()

    var showCreate by remember { mutableStateOf(false) }
    var deleting by remember { mutableStateOf<MatchUiModel?>(null) }

    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Partidos", style = MaterialTheme.typography.headlineSmall)

        Button(
            onClick = { showCreate = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear partido")
        }

        if (matches.isEmpty()) {
            Text(
                "No hay partidos todavía. Pulsa \"Crear partido\" para añadir uno.",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(matches, key = { it.id }) { m ->
                    Card(Modifier.fillMaxWidth()) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(m.location.ifBlank { "Sin ubicación" }, style = MaterialTheme.typography.titleMedium)
                                Spacer(Modifier.height(4.dp))
                                Text("Resultado: ${m.teamAScore} - ${m.teamBScore}")
                                if (m.participants.isNotEmpty()) {
                                    val a = m.participants.filter { it.team == "A" }.joinToString { it.name }
                                    val b = m.participants.filter { it.team == "B" }.joinToString { it.name }
                                    Text("Equipo A: $a")
                                    Text("Equipo B: $b")
                                }
                            }

                            IconButton(onClick = { deleting = m }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }

    if (showCreate) {
        MatchFormDialog(
            vm = vm,
            onDismiss = { showCreate = false },
            onCreated = { showCreate = false }
        )
    }

    deleting?.let { m ->
        AlertDialog(
            onDismissRequest = { deleting = null },
            title = { Text("Eliminar partido") },
            text = { Text("¿Seguro que quieres eliminar este partido?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        vm.deleteMatch(m.id)
                        deleting = null
                    }
                ) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { deleting = null }) { Text("Cancelar") }
            }
        )
    }
}
